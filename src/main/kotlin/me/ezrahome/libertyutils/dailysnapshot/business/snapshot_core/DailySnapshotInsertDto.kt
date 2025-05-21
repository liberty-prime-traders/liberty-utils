package me.ezrahome.libertyutils.dailysnapshot.business.snapshot_core

import me.ezrahome.libertyutils.dailysnapshot.model.LibertyLocation
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

/**
 * DTO for {@link me.ezra_home.daily_snapshot.DailySnapshotEntity}
 */
data class DailySnapshotInsertDto(
    val snapshotDate: LocalDate? = null,
    val startBalanceCash: BigDecimal? = null,
    val endBalanceCash: BigDecimal? = null,
    val outflowCash: BigDecimal? = null,
    val cogs: BigDecimal? = null,
    val cogsReturned: BigDecimal? = null,
    val expenses: BigDecimal? = null,
    val inflowJointAccount: BigDecimal? = null,
    val inflowPersonalAccount: BigDecimal? = null,
    val inflowCreditSales: BigDecimal? = null,
    val location: LibertyLocation? = null
) : Serializable
