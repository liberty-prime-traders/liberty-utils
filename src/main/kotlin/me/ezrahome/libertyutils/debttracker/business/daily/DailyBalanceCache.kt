package me.ezrahome.libertyutils.debttracker.business.daily

import jakarta.annotation.PostConstruct
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionRepository
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionDto
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

@Service
class DailyBalanceCache(
    private val transactionRepository: TransactionRepository,
    private val userLocationUtils: UserLocationUtils
) {
    // Map structure: Location -> Date -> DailyBalanceSummary
    private val dailyBalancesByLocation: MutableMap<LibertyLocation, MutableMap<LocalDate, DailyBalanceSummary>> = ConcurrentHashMap()

    @PostConstruct
    fun init() {
        LibertyLocation.entries.forEach { location ->
            val mapForLocation: MutableMap<LocalDate, DailyBalanceSummary> = ConcurrentHashMap()
            
            // Initialize with existing transaction data
            transactionRepository.findAll().forEach { transaction ->
                if (transaction.location == location && transaction.transactionDate != null) {
                    val date = transaction.transactionDate!!
                    val amount = transaction.amount ?: BigDecimal.ZERO
                    
                    val existingSummary = mapForLocation[date] ?: DailyBalanceSummary(date, BigDecimal.ZERO, BigDecimal.ZERO)
                    
                    val updatedSummary = when (transaction.transactionType) {
                        TransactionType.DEBIT -> existingSummary.copy(
                            totalDebtIssued = existingSummary.totalDebtIssued.add(amount)
                        )
                        TransactionType.CREDIT -> existingSummary.copy(
                            totalDebtCleared = existingSummary.totalDebtCleared.add(amount)
                        )
                        else -> existingSummary
                    }
                    
                    mapForLocation[date] = updatedSummary
                }
            }
            
            dailyBalancesByLocation[location] = mapForLocation
        }
    }

    fun getDailySummaries(startDate: LocalDate, endDate: LocalDate): List<DailyBalanceSummary> {
        val relevantLocations = userLocationUtils.getLocations()
        val dailySummaries = mutableMapOf<LocalDate, DailyBalanceSummary>()
        
        relevantLocations.forEach { location ->
            val locationMap = dailyBalancesByLocation[location] ?: return@forEach
            
            locationMap.forEach { (date, summary) ->
                if (date.isAfter(startDate.minusDays(1)) && date.isBefore(endDate.plusDays(1))) {
                    val existing = dailySummaries[date] ?: DailyBalanceSummary(date, BigDecimal.ZERO, BigDecimal.ZERO)
                    dailySummaries[date] = existing.copy(
                        totalDebtCleared = existing.totalDebtCleared.add(summary.totalDebtCleared),
                        totalDebtIssued = existing.totalDebtIssued.add(summary.totalDebtIssued)
                    )
                }
            }
        }
        
        return dailySummaries.values.sortedBy { it.date }
    }

    fun adjust(oldTransaction: TransactionDto?, newTransaction: TransactionDto?) {
        val oldDate = oldTransaction?.transactionDate
        val newDate = newTransaction?.transactionDate
        val oldLocation = oldTransaction?.location
        val newLocation = newTransaction?.location

        // Remove old transaction if it exists
        if (oldDate != null && oldLocation != null) {
            adjustForTransaction(oldLocation, oldDate, oldTransaction, isRemoval = true)
        }

        // Add new transaction if it exists
        if (newDate != null && newLocation != null) {
            adjustForTransaction(newLocation, newDate, newTransaction, isRemoval = false)
        }
    }

    private fun adjustForTransaction(
        location: LibertyLocation,
        date: LocalDate,
        transaction: TransactionDto,
        isRemoval: Boolean
    ) {
        val locationMap = dailyBalancesByLocation.computeIfAbsent(location) { ConcurrentHashMap() }
        
        val existingSummary = locationMap[date] ?: DailyBalanceSummary(date, BigDecimal.ZERO, BigDecimal.ZERO)
        val amount = transaction.amount ?: BigDecimal.ZERO
        val multiplier = if (isRemoval) BigDecimal(-1) else BigDecimal(1)
        
        val updatedSummary = when (transaction.transactionType) {
            TransactionType.DEBIT -> existingSummary.copy(
                totalDebtIssued = existingSummary.totalDebtIssued.add(amount.multiply(multiplier))
            )
            TransactionType.CREDIT -> existingSummary.copy(
                totalDebtCleared = existingSummary.totalDebtCleared.add(amount.multiply(multiplier))
            )
            else -> existingSummary
        }
        
        locationMap[date] = updatedSummary
    }
}
