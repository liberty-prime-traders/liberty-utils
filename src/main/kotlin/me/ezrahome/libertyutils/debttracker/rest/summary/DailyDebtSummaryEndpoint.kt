package me.ezrahome.libertyutils.debttracker.rest.summary

import me.ezrahome.libertyutils.debttracker.business.dailysummary.DailyDebtSummaryService
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.DailyBalanceResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("secured/debt-summary")
class DailyDebtSummaryEndpoint(
    private val dailyDebtSummaryService: DailyDebtSummaryService
) {
    @GetMapping
    fun getDailySummaries(
        @RequestParam("startDate") startDate: String,
        @RequestParam("endDate") endDate: String
    ): List<DailyBalanceResponseDto> = dailyDebtSummaryService.getSummaries(startDate, endDate)
}


