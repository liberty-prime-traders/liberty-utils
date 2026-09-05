package me.ezrahome.libertyutils.debttracker.business.contact

import jakarta.annotation.PostConstruct
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionRepository
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionDto
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.math.BigDecimal
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class ContactNetStandingCache(
    private val transactionRepository: TransactionRepository,
    private val userLocationUtils: UserLocationUtils
) {
    private val netStandingsByLocation: MutableMap<LibertyLocation, MutableMap<UUID, BigDecimal>> = ConcurrentHashMap()

    @PostConstruct
    fun init() {
        refreshFromDb()
    }

    @Scheduled(cron = "0 0 2 * * *")
    fun refreshFromDb() {
        LibertyLocation.entries.forEach { location ->
            val mapForLocation: MutableMap<UUID, BigDecimal> = ConcurrentHashMap()
            transactionRepository.findAllUserBalancesByLocations(setOf(location)).forEach { projection ->
                mapForLocation[projection.userId] = projection.balance ?: BigDecimal.ZERO
            }
            netStandingsByLocation[location] = mapForLocation
        }
    }

    fun getNetStanding(userId: UUID?): BigDecimal {
        if (userId == null) return BigDecimal.ZERO
        val relevantLocations = userLocationUtils.getLocations()
        return relevantLocations.fold(BigDecimal.ZERO) { acc, location ->
            val standingForLocation = netStandingsByLocation[location]?.get(userId) ?: BigDecimal.ZERO
            acc.add(standingForLocation)
        }
    }

    fun adjust(userId: UUID?, oldTransaction: TransactionDto?, newTransaction: TransactionDto?) {
        if (userId == null) return
        val oldLoc = oldTransaction?.location
        val newLoc = newTransaction?.location

        if (oldLoc == null && newLoc == null) return

        val oldDelta = getChangeInOverallStanding(oldTransaction, null)
        val newDelta = getChangeInOverallStanding(null, newTransaction)

        applyDeltas(userId, oldLoc, oldDelta, newLoc, newDelta)

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
                override fun afterCompletion(status: Int) {
                    // Applied eagerly above so callers (e.g. building a response DTO) see the
                    // up-to-date balance immediately; undo it here if the transaction didn't commit
                    // (rollback, optimistic-lock failure, etc.) to avoid drift from the DB.
                    if (status != TransactionSynchronization.STATUS_COMMITTED) {
                        applyDeltas(userId, oldLoc, oldDelta.negate(), newLoc, newDelta.negate())
                    }
                }
            })
        }
    }

    private fun applyDeltas(userId: UUID, oldLoc: LibertyLocation?, oldDelta: BigDecimal, newLoc: LibertyLocation?, newDelta: BigDecimal) {
        if (oldLoc != null) {
            netStandingsByLocation.computeIfAbsent(oldLoc) { ConcurrentHashMap() }
                .merge(userId, oldDelta, BigDecimal::add)
        }
        if (newLoc != null) {
            netStandingsByLocation.computeIfAbsent(newLoc) { ConcurrentHashMap() }
                .merge(userId, newDelta, BigDecimal::add)
        }
    }

    private fun getChangeInOverallStanding(oldTransaction: TransactionDto?, newTransaction: TransactionDto?): BigDecimal {
        val oldNetValue = getNetChange(oldTransaction)
        val newNetValue = getNetChange(newTransaction)
        return newNetValue.minus(oldNetValue)
    }

    private fun getNetChange(transaction: TransactionDto?): BigDecimal {
        val amount = transaction?.amount ?: BigDecimal.ZERO
        return when (transaction?.transactionType) {
            TransactionType.DEBIT -> amount.negate()
            TransactionType.CREDIT -> amount
            else -> BigDecimal.ZERO
        }
    }
}
