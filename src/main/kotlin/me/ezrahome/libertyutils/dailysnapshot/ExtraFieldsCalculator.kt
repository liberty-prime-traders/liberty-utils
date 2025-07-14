package me.ezrahome.libertyutils.dailysnapshot

import me.ezrahome.libertyutils.dailysnapshot.business.snapshot_core.DailySnapshotResponseDto
import java.math.BigDecimal

object ExtraFieldsCalculator {

    fun calculateAndAppendExtraFields(dailySnapshotResponseDto: DailySnapshotResponseDto) {
        dailySnapshotResponseDto.inflowCash = getCashInflow(dailySnapshotResponseDto)

        dailySnapshotResponseDto.inflowBothAccounts = dailySnapshotResponseDto.inflowJointAccount
            ?.add(dailySnapshotResponseDto.inflowPersonalAccount ?: BigDecimal.ZERO)

        dailySnapshotResponseDto.grossInflow = dailySnapshotResponseDto.inflowCash
            ?.add(dailySnapshotResponseDto.inflowJointAccount ?: BigDecimal.ZERO)
            ?.add(dailySnapshotResponseDto.inflowPersonalAccount ?: BigDecimal.ZERO)
            ?.add(dailySnapshotResponseDto.inflowCreditSales ?: BigDecimal.ZERO)

        dailySnapshotResponseDto.grossOutflow = dailySnapshotResponseDto.cogs
            ?.add(dailySnapshotResponseDto.cogsReturned ?: BigDecimal.ZERO)
            ?.add(dailySnapshotResponseDto.expenses ?: BigDecimal.ZERO)
            ?.add(dailySnapshotResponseDto.transactionCosts ?: BigDecimal.ZERO)

        dailySnapshotResponseDto.netInflow = dailySnapshotResponseDto.grossInflow
            ?.subtract(dailySnapshotResponseDto.grossOutflow ?: BigDecimal.ZERO)
            ?.subtract(dailySnapshotResponseDto.relaySales ?: BigDecimal.ZERO)
    }

    private fun getCashInflow(dailySnapshotResponseDto: DailySnapshotResponseDto): BigDecimal? {
        return dailySnapshotResponseDto.endBalanceCash?.let { endBalance ->
            dailySnapshotResponseDto.startBalanceCash?.let { startBalance ->
                dailySnapshotResponseDto.outflowCash?.let { outflow ->
                    endBalance.subtract(startBalance).add(outflow)
                }
            }
        }
    }
}
