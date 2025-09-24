package me.ezrahome.libertyutils.debttracker.business.transaction

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

@Service
class DailyBalanceCache {

    // HashMap to maintain daily balance data in memory
    private val dailyBalanceMap: MutableMap<LocalDate, DailyBalanceData> = ConcurrentHashMap()

    data class DailyBalanceData(
        var totalDebtCleared: BigDecimal = BigDecimal.ZERO,
        var totalDebtIssued: BigDecimal = BigDecimal.ZERO
    )

    fun getDailyBalance(date: LocalDate): DailyBalanceData {
        return dailyBalanceMap.getOrPut(date) { DailyBalanceData() }
    }

    fun getAllDailyBalances(): Map<LocalDate, DailyBalanceData> {
        return dailyBalanceMap.toMap()
    }

    fun updateDailyBalance(date: LocalDate, debtCleared: BigDecimal, debtIssued: BigDecimal) {
        val balanceData = dailyBalanceMap.getOrPut(date) { DailyBalanceData() }
        balanceData.totalDebtCleared = balanceData.totalDebtCleared.add(debtCleared)
        balanceData.totalDebtIssued = balanceData.totalDebtIssued.add(debtIssued)
    }

    fun removeDailyBalance(date: LocalDate, debtCleared: BigDecimal, debtIssued: BigDecimal) {
        val balanceData = dailyBalanceMap[date]
        if (balanceData != null) {
            balanceData.totalDebtCleared = balanceData.totalDebtCleared.subtract(debtCleared)
            balanceData.totalDebtIssued = balanceData.totalDebtIssued.subtract(debtIssued)
            
            // Remove entry if both values are zero
            if (balanceData.totalDebtCleared == BigDecimal.ZERO && balanceData.totalDebtIssued == BigDecimal.ZERO) {
                dailyBalanceMap.remove(date)
            }
        }
    }

    fun getDailyBalancesInRange(startDate: LocalDate, endDate: LocalDate): Map<LocalDate, DailyBalanceData> {
        return dailyBalanceMap.filterKeys { date ->
            !date.isBefore(startDate) && !date.isAfter(endDate)
        }
    }
}
