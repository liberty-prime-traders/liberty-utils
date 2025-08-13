package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import me.ezrahome.libertyutils.dailysnapshot.model.LibertyLocation
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

/**
 * DTO for {@link me.ezra_home.debttracker.TransactionEntity}
 */
data class TransactionResponseDto (
    var id: UUID? = null,
    var userId: UUID? = null,
    var contactName: String? = null,
    var transactionType: TransactionType? = null,
    var transactionDate: LocalDate? = null,
    var amount: BigDecimal? = null,
    var description: String? = null,
    var location: LibertyLocation? = null
)