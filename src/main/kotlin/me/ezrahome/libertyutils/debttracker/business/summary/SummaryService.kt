package me.ezrahome.libertyutils.debttracker.business.summary

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import me.ezrahome.libertyutils.debttracker.business.contact.ContactCache
import me.ezrahome.libertyutils.debttracker.business.summary.dto.SummaryDto
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionCache
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class SummaryService (
    private val contactCache: ContactCache,
    private val transactionCache: TransactionCache
) {
    private final val formatter = DateTimeFormatter.ofPattern("h:mm:ss a")
    suspend fun generateDashboardSummary(): SummaryDto = coroutineScope {
        val summaryStats = async { transactionCache.getAllTransactions() }
        val latestTransactions = async { transactionCache.getLatestTransactions() }
        val totalContacts = async { contactCache.getAllContacts().count() }
        val topDebtors = async { transactionCache.findTopUsersByType(TransactionType.DEBIT) }
        val topCreditors = async { transactionCache.findTopUsersByType(TransactionType.CREDIT) }

        val stats = summaryStats.await()
        val debtors = stats.filter { it.transactionType == TransactionType.DEBIT }
        val creditors = stats.filter { it.transactionType == TransactionType.CREDIT }
        val time = LocalTime.now()


        SummaryDto(
            timeFetched = time.format(formatter),
            latestTransactions = latestTransactions.await(),
            totalContacts = totalContacts.await(),
            totalDebtors = debtors.map { it.userId }.toSet().count(),
            totalCreditors = creditors.map { it.userId }.toSet().count(),
            totalDebt = debtors.mapNotNull { it.amount }.fold(BigDecimal.ZERO, BigDecimal::add),
            totalCredit = creditors.mapNotNull { it.amount }.fold(BigDecimal.ZERO, BigDecimal::add),
            topDebtors = topDebtors.await(),
            topCreditors = topCreditors.await()
        )
    }
}