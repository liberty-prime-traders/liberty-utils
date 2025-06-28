package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import me.ezrahome.libertyutils.debttracker.model.TransactionTypes
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

/**
 * DTO for {@link me.ezra_home.debttracker.TransactionEntity}
 */
data class TransactionResponseDto (
    var id: UUID? = null,
    var userID: UUID? = null,
    var transactionType: TransactionTypes? = null,
    var transactionDate: LocalDate? = null,
    var amount: BigDecimal? = null,
    var description: String? = null
)