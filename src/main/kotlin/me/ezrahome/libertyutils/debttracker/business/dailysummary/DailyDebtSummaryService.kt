package me.ezrahome.libertyutils.debttracker.business.dailysummary

import me.ezrahome.libertyutils.debttracker.business.transaction.dto.DailyBalanceResponseDto
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Service
class DailyDebtSummaryService(
    private val dailyDebtSummaryCache: DailyDebtSummaryCache,
    private val userLocationUtils: UserLocationUtils
) {

    @Transactional(readOnly = true)
    fun getSummaries(startDate: String, endDate: String): List<DailyBalanceResponseDto> {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        require(!end.isBefore(start)) { "endDate must be on or after startDate" }

        val locations = userLocationUtils.getLocations()

        val dates: List<LocalDate> = generateSequence(start) { current ->
            val next = current.plusDays(1)
            if (next.isAfter(end)) null else next
        }.toList().let { list -> if (list.isEmpty()) listOf(start) else listOf(start) + list }

        return dates.map { date ->
            var totalCleared = BigDecimal.ZERO
            var totalIssued = BigDecimal.ZERO

            locations.forEach { location ->
                val perLocation = dailyDebtSummaryCache.get(location)[date]
                if (perLocation != null) {
                    totalCleared = totalCleared.add(perLocation.debtCleared)
                    totalIssued = totalIssued.add(perLocation.debtIssued)
                }
            }

            val net = totalIssued.subtract(totalCleared)
            DailyBalanceResponseDto(
                date = date,
                totalDebtCleared = totalCleared,
                totalDebtIssued = totalIssued,
                netBalance = net
            )
        }
    }
}


