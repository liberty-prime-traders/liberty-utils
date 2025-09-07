package me.ezrahome.libertyutils.debttracker.business.transaction.dto

import me.ezrahome.libertyutils.debttracker.model.TransactionType
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import java.math.BigDecimal

data class TransactionDto(
    val amount: BigDecimal?,
    val transactionType: TransactionType?,
    val location: LibertyLocation?
)
