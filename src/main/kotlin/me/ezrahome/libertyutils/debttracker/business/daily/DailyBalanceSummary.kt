package me.ezrahome.libertyutils.debttracker.business.daily

import java.math.BigDecimal
import java.time.LocalDate

data class DailyBalanceSummary(
    val date: LocalDate,
    val totalDebtCleared: BigDecimal,
    val totalDebtIssued: BigDecimal
)
