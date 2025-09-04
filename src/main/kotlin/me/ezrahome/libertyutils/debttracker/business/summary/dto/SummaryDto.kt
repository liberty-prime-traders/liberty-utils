package me.ezrahome.libertyutils.debttracker.business.summary.dto

import java.math.BigDecimal

data class SummaryDto(
    val timeFetched: String,
    val latestTransactions: List<LatestTransactionsDto>,
    val totalContacts: Number,
    val totalDebtors: Number,
    val totalCreditors: Number,
    val totalDebt: BigDecimal,
    val totalCredit: BigDecimal,
    val topDebtors: List<TopContactDto>,
    val topCreditors: List<TopContactDto>
)