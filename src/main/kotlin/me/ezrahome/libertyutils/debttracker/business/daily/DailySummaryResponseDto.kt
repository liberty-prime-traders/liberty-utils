package me.ezrahome.libertyutils.debttracker.business.daily

import java.math.BigDecimal
import java.time.LocalDate

data class DailySummaryResponseDto(
    val date: LocalDate,
    val totalDebtCleared: BigDecimal,
    val totalDebtIssued: BigDecimal
)
