package me.ezra_home.daily_snapshot

import java.math.BigDecimal

object ExtraFieldsCalculator {

    fun calculateAndAppendExtraFields(dailySnapshotResponseDto: DailySnapshotResponseDto) {
        dailySnapshotResponseDto.inflowCash = getCashInflow(dailySnapshotResponseDto)
        dailySnapshotResponseDto.inflowJointAccount = getJointAccountInflow(dailySnapshotResponseDto)
        dailySnapshotResponseDto.inflowPersonalAccount = getPersonalAccountInflow(dailySnapshotResponseDto)

        dailySnapshotResponseDto.totalInflow = dailySnapshotResponseDto.inflowCash
            ?.add(dailySnapshotResponseDto.inflowJointAccount ?: BigDecimal.ZERO)
            ?.add(dailySnapshotResponseDto.inflowPersonalAccount ?: BigDecimal.ZERO)
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

    private fun getJointAccountInflow(dailySnapshotResponseDto: DailySnapshotResponseDto): BigDecimal? {
        return dailySnapshotResponseDto.endBalanceJointAccount?.let { endBalance ->
            dailySnapshotResponseDto.startBalanceJointAccount?.let { startBalance ->
                dailySnapshotResponseDto.outflowJointAccount?.let { outflow ->
                    endBalance.subtract(startBalance).add(outflow)
                }
            }
        }
    }

    private fun getPersonalAccountInflow(dailySnapshotResponseDto: DailySnapshotResponseDto): BigDecimal? {
        return dailySnapshotResponseDto.endBalancePersonalAccount?.let { endBalance ->
            dailySnapshotResponseDto.startBalancePersonalAccount?.let { startBalance ->
                dailySnapshotResponseDto.outflowPersonalAccount?.let { outflow ->
                    endBalance.subtract(startBalance).add(outflow)
                }
            }
        }
    }
}
