package me.ezrahome.libertyutils.debttracker.business.dailysummary

import java.math.BigDecimal
import java.time.LocalDate

data class DailyDebtSummary(
    val date: LocalDate,
    val debtCleared: BigDecimal,
    val debtIssued: BigDecimal
)
