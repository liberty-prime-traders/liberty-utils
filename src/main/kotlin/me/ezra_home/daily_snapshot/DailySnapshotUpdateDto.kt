package me.ezra_home.daily_snapshot

import java.time.LocalDate
import java.util.Optional
import java.util.UUID

data class DailySnapshotUpdateDto(
    val id: UUID? = null,
    val snapshotDate: Optional<LocalDate>? = null,
    val startBalanceCash: Optional<Double>? = null,
    val startBalanceJointAccount: Optional<Double>? = null,
    val startBalancePersonalAccount: Optional<Double>? = null,
    val endBalanceCash: Optional<Double>? = null,
    val endBalanceJointAccount: Optional<Double>? = null,
    val endBalancePersonalAccount: Optional<Double>? = null,
    val outflowCash: Optional<Double>? = null,
    val outflowJointAccount: Optional<Double>? = null,
    val outflowPersonalAccount: Optional<Double>? = null
)
