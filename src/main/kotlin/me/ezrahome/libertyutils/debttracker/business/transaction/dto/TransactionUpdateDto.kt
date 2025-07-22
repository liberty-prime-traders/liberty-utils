package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import me.ezrahome.libertyutils.debttracker.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

data class TransactionUpdateDto (
    var id: UUID? = null,
    var userId: Optional<UUID>? = null,
    var transactionType: Optional<TransactionType>? = null,
    var transactionDate: Optional<LocalDate>? = null,
    var amount: Optional<BigDecimal>? = null,
    var description: Optional<String>? = null
)