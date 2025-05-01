package me.ezra_home.daily_snapshot

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
    var startBalanceJointAccount: BigDecimal? = null,
    var startBalancePersonalAccount: BigDecimal? = null,
    var endBalanceCash: BigDecimal? = null,
    var endBalanceJointAccount: BigDecimal? = null,
    var endBalancePersonalAccount: BigDecimal? = null,
    var outflowCash: BigDecimal? = null,
    var outflowJointAccount: BigDecimal? = null,
    var outflowPersonalAccount: BigDecimal? = null,
    var inflowCash: BigDecimal? = null,
    var inflowJointAccount: BigDecimal? = null,
    var inflowPersonalAccount: BigDecimal? = null,
    var totalInflow: BigDecimal? = null
) : Serializable
