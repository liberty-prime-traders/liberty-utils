package me.ezrahome.libertyutils.debttracker.business.summary.dto

import java.math.BigDecimal

data class SummaryDto(
    val latestTransactions: List<LatestTransactionsDto>,
    val totalContacts: Long,
    val totalDebtors: Long,
    val totalCreditors: Long,
    val totalDebt: BigDecimal,
    val totalCredit: BigDecimal,
    val net: BigDecimal,
    val topDebtors: List<TopUserDto>,
    val topCreditors: List<TopUserDto>
)