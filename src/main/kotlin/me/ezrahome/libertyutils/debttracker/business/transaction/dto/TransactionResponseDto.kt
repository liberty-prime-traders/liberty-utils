package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import java.math.BigDecimal
import java.util.UUID

/**
 * DTO for {@link me.ezra_home.debttracker.TransactionEntity}
 */
data class TransactionResponseDto (
    var id: UUID? = null,
    var userId: UUID? = null,
    var contactName: String? = null,
    var contactBalance: BigDecimal? = null,
    var transactionType: TransactionType? = null,
    var transactionDate: String? = null,
    var amount: BigDecimal? = null,
    var description: String? = null,
    var location: LibertyLocation? = null
)
