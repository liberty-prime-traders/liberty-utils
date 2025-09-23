package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import java.math.BigDecimal
import java.time.LocalDate

data class DailyBalanceResponseDto(
    val date: LocalDate,
    val totalDebtCleared: BigDecimal,
    val totalDebtIssued: BigDecimal,
    val netBalance: BigDecimal
)
