package me.ezrahome.libertyutils.debttracker.business.dailysummary

import jakarta.annotation.PostConstruct
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionRepository
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionDto
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

@Component
class DailyDebtSummaryCache(private val transactionRepository: TransactionRepository) {

    private val dailyDebtSummariesByLocation: MutableMap<LibertyLocation, MutableMap<LocalDate, DailyDebtSummary>> = ConcurrentHashMap()

    @PostConstruct
    fun init() {
        LibertyLocation.entries.forEach { getMutable(it) }
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val transactions = transactionRepository.findByTransactionDateGreaterThanEqualAndTransactionDateLessThanEqual(
            startOfMonth, today
        )

        transactions.forEach { transaction ->
            val location = transaction.location!!
            val date = transaction.transactionDate!!
            val amount = transaction.amount ?: BigDecimal.ZERO
            val locationMap = getMutable(location)
            val summaryToUpdate = locationMap[date] ?: DailyDebtSummary(date, BigDecimal.ZERO, BigDecimal.ZERO)
            val updatedSummary = when (transaction.transactionType) {
                TransactionType.DEBIT -> summaryToUpdate.copy(
                    debtIssued = summaryToUpdate.debtIssued.add(amount)
                )
                TransactionType.CREDIT -> summaryToUpdate.copy(
                    debtCleared = summaryToUpdate.debtCleared.add(amount)
                )
                else -> summaryToUpdate
            }
            set(location, date, updatedSummary)
        }
    }

    fun getMutable(location: LibertyLocation?): MutableMap<LocalDate, DailyDebtSummary> {
        if (location == null) {
            return ConcurrentHashMap()
        }
        return dailyDebtSummariesByLocation.computeIfAbsent(location) { ConcurrentHashMap() }
    }

     fun get(location: LibertyLocation?): Map<LocalDate, DailyDebtSummary> = getMutable(location).toMap()

    private fun set(location: LibertyLocation, date: LocalDate, summary: DailyDebtSummary) {
        getMutable(location)[date] = summary
    }

    fun upsertTransaction(transaction: TransactionDto) {
        val locationMap = getMutable(transaction.location)
        val transactionDate = transaction.transactionDate!!
        val existing = locationMap[transactionDate] ?: DailyDebtSummary(transactionDate, BigDecimal.ZERO, BigDecimal.ZERO)
        val updated = when (transaction.transactionType) {
            TransactionType.DEBIT -> existing.copy(debtIssued = existing.debtIssued.add(transaction.amount))
            TransactionType.CREDIT -> existing.copy(debtCleared = existing.debtCleared.add(transaction.amount))
            else -> existing
        }
        set(transaction.location!!, transactionDate, updated)
    }

    fun findMissingDates(locations: Set<LibertyLocation>, requestedDates: Set<LocalDate>): Set<LocalDate> {
        val missingDates = requestedDates.toMutableSet()
        locations.forEach { missingDates.removeAll(getMutable(it).keys) }
        return missingDates
    }
    
    fun adjust(oldTransaction: TransactionDto?, newTransaction: TransactionDto?) {
        // Remove effect of old transaction (edit/delete)
        if (oldTransaction?.transactionDate != null && oldTransaction.location != null) {
            adjustForTransaction(oldTransaction, isRemoval = true)
        }
        // Apply effect of new transaction (insert/edit)
        if (newTransaction?.transactionDate != null && newTransaction.location != null) {
            adjustForTransaction(newTransaction, isRemoval = false)
        }
    }

    private fun adjustForTransaction(
        transaction: TransactionDto,
        isRemoval: Boolean
    ) {
        val location = requireNotNull(transaction.location)
        val date = requireNotNull(transaction.transactionDate)
        val amount = transaction.amount ?: BigDecimal.ZERO
        val txType = transaction.transactionType

        val locationMap = getMutable(location)
        val existingSummary = locationMap[date] ?: DailyDebtSummary(date, BigDecimal.ZERO, BigDecimal.ZERO)
        val signedAmount = if (isRemoval) amount.negate() else amount

        val updatedSummary = when (txType) {
            TransactionType.DEBIT -> existingSummary.copy(
                debtIssued = existingSummary.debtIssued.add(signedAmount)
            )
            TransactionType.CREDIT -> existingSummary.copy(
                debtCleared = existingSummary.debtCleared.add(signedAmount)
            )
            else -> existingSummary
        }

        // Remove entry when both totals are zero; otherwise set updated
        if (updatedSummary.debtCleared == BigDecimal.ZERO && updatedSummary.debtIssued == BigDecimal.ZERO) {
            locationMap.remove(date)
        } else {
            locationMap[date] = updatedSummary
        }
    }
}
