package me.ezrahome.libertyutils.dailysnapshot

import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * DTO for {@link me.ezra_home.daily_snapshot.DailySnapshotEntity}
 */
data class DailySnapshotResponseDto(
    var id: UUID? = null,
    var snapshotDate: String? = null,
    var createdOn: Instant? = null,
    var startBalanceCash: BigDecimal? = null,
    var endBalanceCash: BigDecimal? = null,
    var outflowCash: BigDecimal? = null,
    var cogs: BigDecimal? = null,
    var cogsReturned: BigDecimal? = null,
    var expenses: BigDecimal? = null,
    var grossOutflow: BigDecimal? = null,
    var inflowJointAccount: BigDecimal? = null,
    var inflowPersonalAccount: BigDecimal? = null,
    var inflowBothAccounts: BigDecimal? = null,
    var inflowCash: BigDecimal? = null,
    var inflowCreditSales: BigDecimal? = null,
    var grossInflow: BigDecimal? = null,
    var netInflow: BigDecimal? = null
) : Serializable
