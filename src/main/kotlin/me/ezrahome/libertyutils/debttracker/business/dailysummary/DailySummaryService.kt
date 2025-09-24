package me.ezrahome.libertyutils.debttracker.business.dailysummary

import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionRepository
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionDto
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import me.ezrahome.libertyutils.reusable.BatchExecutor
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class DailySummaryService(
    private val dailyDebtSummaryCache: DailyDebtSummaryCache,
    private val transactionRepository: TransactionRepository,
    private val userLocationUtils: UserLocationUtils
) {
    
    fun getDailySummaries(startDate: LocalDate, endDate: LocalDate): Collection<DailyDebtSummary> {
        val relevantLocations = userLocationUtils.getLocations()
        val requestedDates = generateDateSet(startDate, endDate)
        val missingDates = dailyDebtSummaryCache.findMissingDates(relevantLocations, requestedDates)
        if (missingDates.isNotEmpty()) {
            loadMissingDates(relevantLocations, missingDates)
        }
        return aggregateSummaries(relevantLocations, requestedDates)
    }

    private fun generateDateSet(startDate: LocalDate, endDate: LocalDate): Set<LocalDate> {
        return generateSequence(startDate) { prev ->
            if (prev.isBefore(endDate)) prev.plusDays(1) else null
        }.toSet()
    }

    private fun loadMissingDates(locations: Set<LibertyLocation>, missingDates: Set<LocalDate>) {
        val transactions = BatchExecutor.findAllByKeyIn(missingDates) { datesBatch ->
            transactionRepository.findByTransactionDateInAndLocationIn(datesBatch, locations)
        }
        transactions.forEach { transaction ->
            val transactionDto = TransactionDto(
                transaction.amount ?: BigDecimal.ZERO,
                transaction.transactionType,
                transaction.location,
                transaction.transactionDate
            )
            dailyDebtSummaryCache.upsertTransaction(transactionDto)
        }
    }

    private fun aggregateSummaries(locations: Set<LibertyLocation>, requestedDates: Set<LocalDate>): Collection<DailyDebtSummary> {
        val summaries = mutableMapOf<LocalDate, DailyDebtSummary>()
        requestedDates.forEach { date ->
            val summary = summaries[date] ?: DailyDebtSummary(date, BigDecimal.ZERO, BigDecimal.ZERO)
            locations.forEach { location ->
                dailyDebtSummaryCache.get(location)[date]?.run {
                    summaries[date] = summary.copy(
                        debtIssued = summary.debtIssued.add(this.debtIssued),
                        debtCleared = summary.debtCleared.add(this.debtCleared)
                    )
                }
            }
        }
        return summaries.values
    }
}
