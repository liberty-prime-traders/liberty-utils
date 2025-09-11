package me.ezrahome.libertyutils.debttracker.business.daily

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DailySummaryService(
    private val dailyBalanceCache: DailyBalanceCache
) {
    
    fun getDailySummaries(startDate: LocalDate, endDate: LocalDate): List<DailySummaryResponseDto> {
        return dailyBalanceCache.getDailySummaries(startDate, endDate)
            .map { summary ->
                DailySummaryResponseDto(
                    date = summary.date,
                    totalDebtCleared = summary.totalDebtCleared,
                    totalDebtIssued = summary.totalDebtIssued
                )
            }
    }
}
