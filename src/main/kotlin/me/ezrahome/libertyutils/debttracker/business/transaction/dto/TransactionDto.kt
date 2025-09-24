package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import me.ezrahome.libertyutils.debttracker.model.TransactionType
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import java.math.BigDecimal
import java.time.LocalDate

data class TransactionDto(
    val amount: BigDecimal?,
    val transactionType: TransactionType?,
    val location: LibertyLocation?,
    val transactionDate: LocalDate?
)
