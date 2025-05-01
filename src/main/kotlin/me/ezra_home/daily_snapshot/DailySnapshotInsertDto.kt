package me.ezra_home.daily_snapshot

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

/**
 * DTO for {@link me.ezra_home.daily_snapshot.DailySnapshotEntity}
 */
data class DailySnapshotInsertDto(
    val snapshotDate: LocalDate? = null,
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
    val outflowPersonalAccount: BigDecimal? = null
) : Serializable
