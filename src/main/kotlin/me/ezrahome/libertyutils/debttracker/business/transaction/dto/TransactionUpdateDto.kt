package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import me.ezrahome.libertyutils.debttracker.model.TransactionType
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

data class TransactionUpdateDto (
    var id: UUID,
    var transactionType: Optional<TransactionType>? = null,
    var transactionDate: Optional<LocalDate>? = null,
    var amount: Optional<BigDecimal>? = null,
    var description: Optional<String>? = null,
    var location: Optional<LibertyLocation>? = null
)
