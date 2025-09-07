package me.ezrahome.libertyutils.debttracker.business.contact

import jakarta.annotation.PostConstruct
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionRepository
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionDto
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import org.springframework.stereotype.Service
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

        if (oldLoc != null) {
            netStandingsByLocation.computeIfAbsent(oldLoc) { ConcurrentHashMap() }
                .compute(userId) { _, existing ->
                    val current = existing ?: BigDecimal.ZERO
                    current.add(oldDelta.negate())
                }
        }
        if (newLoc != null) {
            netStandingsByLocation.computeIfAbsent(newLoc) { ConcurrentHashMap() }
                .compute(userId) { _, existing ->
                    val current = existing ?: BigDecimal.ZERO
                    current.add(newDelta)
                }
        }
    }

    private fun getChangeInOverallStanding(oldTransaction: TransactionDto?, newTransaction: TransactionDto?): BigDecimal {
        val oldAmount = oldTransaction?.amount ?: BigDecimal.ZERO
        val newAmount = newTransaction?.amount ?: BigDecimal.ZERO

        val oldNetValue = when (oldTransaction?.transactionType) {
            TransactionType.DEBIT -> oldAmount.negate()
            TransactionType.CREDIT -> oldAmount
            else -> BigDecimal.ZERO
        }

        val newNetValue = when (newTransaction?.transactionType) {
            TransactionType.DEBIT -> newAmount.negate()
            TransactionType.CREDIT -> newAmount
            else -> BigDecimal.ZERO
        }

        return newNetValue.minus(oldNetValue)
    }
}
