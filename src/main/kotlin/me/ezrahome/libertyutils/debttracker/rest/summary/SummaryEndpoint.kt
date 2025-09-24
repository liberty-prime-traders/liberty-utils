package me.ezrahome.libertyutils.debttracker.rest.summary

import me.ezrahome.libertyutils.debttracker.business.dashboardsummary.DashboardSummaryService
import me.ezrahome.libertyutils.debttracker.business.dashboardsummary.DashboardSummaryDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("secured/summary")
class SummaryEndpoint(
    private val dashboardSummaryService: DashboardSummaryService
) {
    @GetMapping
    suspend fun getDashboardSummary(): DashboardSummaryDto {
        return dashboardSummaryService.generateDashboardSummary()
    }
}
