package me.ezrahome.libertyutils.dailysnapshot

import java.math.BigDecimal
import java.util.Optional
import java.util.UUID

data class DailySnapshotUpdateDto(
    val id: UUID? = null,
    val startBalanceCash: Optional<BigDecimal>? = null,
    val endBalanceCash: Optional<BigDecimal>? = null,
    val outflowCash: Optional<BigDecimal>? = null,
    val cogs: Optional<BigDecimal>? = null,
    val cogsReturned: Optional<BigDecimal>? = null,
    val expenses: Optional<BigDecimal>? = null,
    val inflowJointAccount: Optional<BigDecimal>? = null,
    val inflowPersonalAccount: Optional<BigDecimal>? = null,
    val inflowCreditSales: Optional<BigDecimal>? = null
)
