package me.ezrahome.libertyutils.debttracker.rest.summary

import me.ezrahome.libertyutils.debttracker.business.summary.SummaryService
import me.ezrahome.libertyutils.debttracker.business.summary.dto.SummaryDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("secured/summary")
class SummaryEndpoint(
    private val summaryService: SummaryService
) {
    @GetMapping
    suspend fun getDashboardSummary(): SummaryDto {
        return summaryService.generateDashboardSummary()
    }
}