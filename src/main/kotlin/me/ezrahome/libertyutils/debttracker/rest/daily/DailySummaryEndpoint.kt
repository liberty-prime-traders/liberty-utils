package me.ezrahome.libertyutils.debttracker.rest.daily

import me.ezrahome.libertyutils.debttracker.business.daily.DailySummaryResponseDto
import me.ezrahome.libertyutils.debttracker.business.daily.DailySummaryService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
    ): ResponseEntity<List<DailySummaryResponseDto>> {
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build()
        }
        
        val summaries = dailySummaryService.getDailySummaries(startDate, endDate)
        return ResponseEntity.ok(summaries)
    }
}
