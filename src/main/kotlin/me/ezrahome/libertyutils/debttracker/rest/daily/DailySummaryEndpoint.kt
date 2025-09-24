package me.ezrahome.libertyutils.debttracker.rest.daily

import me.ezrahome.libertyutils.debttracker.business.dailysummary.DailyDebtSummary
import me.ezrahome.libertyutils.debttracker.business.dailysummary.DailySummaryService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("secured/daily-summaries")
class DailySummaryEndpoint(
    private val dailySummaryService: DailySummaryService
) {

    @GetMapping
    fun getDailySummaries(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): Collection<DailyDebtSummary> {
        return if (startDate.isAfter(endDate)) listOf()
            else dailySummaryService.getDailySummaries(startDate, endDate)
    }
}
