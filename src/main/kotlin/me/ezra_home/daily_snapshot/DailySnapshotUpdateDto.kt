package me.ezra_home.daily_snapshot

import java.util.Optional
import java.util.UUID

data class DailySnapshotUpdateDto(
    val id: UUID? = null,
    val startBalanceCash: Optional<Double>? = null,
    val endBalanceCash: Optional<Double>? = null,
    val outflowCash: Optional<Double>? = null,
    val cogs: Optional<Double>? = null,
    val cogsReturned: Optional<Double>? = null,
    val expenses: Optional<Double>? = null,
    val inflowJointAccount: Optional<Double>? = null,
    val inflowPersonalAccount: Optional<Double>? = null,
    val inflowCreditSales: Optional<Double>? = null
)
