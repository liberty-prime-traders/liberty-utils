package me.ezrahome.libertyutils.debttracker.business.summary.dto

import me.ezrahome.libertyutils.debttracker.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class LatestTransactionsDto (
    var id: UUID? = null,
    var amount: BigDecimal? = null,
    var transactionDate: LocalDate? = null,
    var description: String? = null,
    var transactionType: TransactionType? = null,
    var contactName: String? = null
)
