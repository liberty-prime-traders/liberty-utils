package me.ezrahome.libertyutils.debttracker.business.dashboardsummary

import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import java.math.BigDecimal
import java.time.OffsetDateTime

data class DashboardSummaryDto(
    val timeFetched: OffsetDateTime,
    val latestTransactions: List<TransactionResponseDto>,
    val totalDebtors: Number,
    val totalCreditors: Number,
    val totalOwedToMe: BigDecimal,
    val totalOwedByMe: BigDecimal,
    val myNetStanding: BigDecimal,
    val topDebtors: List<TopContactDto>,
    val topCreditors: List<TopContactDto>
)
