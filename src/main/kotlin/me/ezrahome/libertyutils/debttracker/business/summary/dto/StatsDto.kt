package me.ezrahome.libertyutils.debttracker.business.summary.dto

import java.math.BigDecimal

data class StatsDto(
    val totalDebt: BigDecimal,
    val totalCredit: BigDecimal,
    val totalDebtors: Long,
    val totalCreditors: Long
)