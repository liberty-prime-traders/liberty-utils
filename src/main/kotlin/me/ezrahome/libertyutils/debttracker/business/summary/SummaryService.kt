package me.ezrahome.libertyutils.debttracker.business.summary

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import me.ezrahome.libertyutils.debttracker.business.contact.ContactRepository
import me.ezrahome.libertyutils.debttracker.business.summary.dto.SummaryDto
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionCache
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import org.springframework.stereotype.Service

@Service
class SummaryService (
    private val contactRepository: ContactRepository,
    private val transactionCache: TransactionCache
) {
    suspend fun generateDashboardSummary(): SummaryDto = coroutineScope {
        val summaryStats = async { transactionCache.getSummaryStats() }
        val latestTransactions = async { transactionCache.getLatestTransactions() }
        val totalContacts = async { contactRepository.count() }
        val topDebtors = async { transactionCache.findTopUsersByType(TransactionType.DEBIT) }
        val topCreditors = async { transactionCache.findTopUsersByType(TransactionType.CREDIT) }

        val stats = summaryStats.await()

        SummaryDto(
            latestTransactions = latestTransactions.await(),
            totalContacts = totalContacts.await(),
            totalDebtors = stats.totalDebtors,
            totalCreditors = stats.totalCreditors,
            totalDebt = stats.totalDebt,
            totalCredit = stats.totalCredit,
            net = stats.totalDebt - stats.totalCredit,
            topDebtors = topDebtors.await(),
            topCreditors = topCreditors.await()
        )
    }
}