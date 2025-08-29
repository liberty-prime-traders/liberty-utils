package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class TransactionInsertDto(
    val userId: UUID? = null,
    val transactionType: TransactionType? = null,
    val transactionDate: LocalDate? = null,
    val amount: BigDecimal? = null,
    val description: String? = null,
    val location: LibertyLocation? = null
): Serializable
