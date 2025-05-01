package me.ezra_home.daily_snapshot

import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * DTO for {@link me.ezra_home.daily_snapshot.DailySnapshotEntity}
 */
data class DailySnapshotResponseDto(
    val id: UUID? = null,
    val snapshotDate: LocalDate? = null,
    val createdOn: Instant? = null,
    val cogs: BigDecimal? = null,
    val cogsReturned: BigDecimal? = null,
    val expenses: BigDecimal? = null,
    val startBalanceCash: BigDecimal? = null,
    val startBalanceJointAccount: BigDecimal? = null,
    val startBalancePersonalAccount: BigDecimal? = null,
    val endBalanceCash: BigDecimal? = null,
    val endBalanceJointAccount: BigDecimal? = null,
    val endBalancePersonalAccount: BigDecimal? = null,
    val outflowCash: BigDecimal? = null,
    val outflowJointAccount: BigDecimal? = null,
    val outflowPersonalAccount: BigDecimal? = null,
    val inflowCash: BigDecimal? = null,
    val inflowJointAccount: BigDecimal? = null,
    val inflowPersonalAccount: BigDecimal? = null,
    val totalInflow: BigDecimal? = null,
    val totalOutflow: BigDecimal? = null,
    val netChange: BigDecimal? = null
) : Serializable
