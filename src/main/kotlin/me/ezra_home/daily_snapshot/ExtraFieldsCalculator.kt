package me.ezra_home.daily_snapshot

import java.math.BigDecimal

object ExtraFieldsCalculator {

    fun calculateAndAppendExtraFields(dailySnapshotResponseDto: DailySnapshotResponseDto) {
        dailySnapshotResponseDto.inflowCash = getCashInflow(dailySnapshotResponseDto)

        dailySnapshotResponseDto.grossInflow = dailySnapshotResponseDto.inflowCash
            ?.add(dailySnapshotResponseDto.inflowJointAccount ?: BigDecimal.ZERO)
            ?.add(dailySnapshotResponseDto.inflowPersonalAccount ?: BigDecimal.ZERO)
            ?.add(dailySnapshotResponseDto.inflowCreditSales ?: BigDecimal.ZERO)

        dailySnapshotResponseDto.netInflow = dailySnapshotResponseDto.grossInflow
            ?.subtract(dailySnapshotResponseDto.cogs ?: BigDecimal.ZERO)
            ?.subtract(dailySnapshotResponseDto.cogsReturned ?: BigDecimal.ZERO)
            ?.subtract(dailySnapshotResponseDto.expenses ?: BigDecimal.ZERO)
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
