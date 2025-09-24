package me.ezrahome.libertyutils.debttracker.business.dashboardsummary

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import me.ezrahome.libertyutils.debttracker.business.contact.ContactCache
import me.ezrahome.libertyutils.debttracker.business.contact.ContactNetStandingCache
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionCache
import me.ezrahome.libertyutils.debttracker.business.transaction.mapping.TransactionMapper
import me.ezrahome.libertyutils.debttracker.model.ContactEntity
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.OffsetDateTime

@Service
class DashboardSummaryService (
    private val contactCache: ContactCache,
    private val transactionCache: TransactionCache,
    private val contactNetStandingCache: ContactNetStandingCache,
    private val transactionMapper: TransactionMapper,
    private val userLocationUtils: UserLocationUtils
) {
    suspend fun generateDashboardSummary(): DashboardSummaryDto = coroutineScope {
        val latestTransactionDeferred = async { transactionCache.getLatestTransactions(userLocationUtils.getLocations()) }
        val contactsDeferred = async { contactCache.getAllContacts() }

        val contacts = contactsDeferred.await()
        val standingsByContact: Map<ContactEntity, BigDecimal> = contacts.associateWith { contactNetStandingCache.getNetStanding(it.id) }
        val contactHasNegativeStanding = { amount: BigDecimal -> amount < BigDecimal.ZERO }
        val contactHasPositiveStanding = { amount: BigDecimal -> amount > BigDecimal.ZERO }

        val totalDebtors = standingsByContact.values.count(contactHasNegativeStanding)
        val totalCreditors = standingsByContact.values.count (contactHasPositiveStanding)

        val topDebtors = buildTopList(standingsByContact, contactHasNegativeStanding)
        val topCreditors = buildTopList(standingsByContact, contactHasPositiveStanding)

        val totalOwedToMe = standingsByContact.values.filter(contactHasNegativeStanding)
            .fold(BigDecimal.ZERO, BigDecimal::add)
            .abs()
        val totalOwedByMe = standingsByContact.values.filter(contactHasPositiveStanding)
            .fold(BigDecimal.ZERO, BigDecimal::add)
            .abs()
            .negate()
        val myNetStanding = totalOwedToMe.add(totalOwedByMe)

        val latestTransactions = latestTransactionDeferred.await().map { transactionMapper.toResponseDto(it) }

        DashboardSummaryDto(
            timeFetched = OffsetDateTime.now(),
            latestTransactions = latestTransactions,
            totalDebtors = totalDebtors,
            totalCreditors = totalCreditors,
            totalOwedToMe = totalOwedToMe,
            totalOwedByMe = totalOwedByMe,
            myNetStanding = myNetStanding,
            topDebtors = topDebtors,
            topCreditors = topCreditors
        )
    }

    private fun buildTopList(
        balancesByContact: Map<ContactEntity, BigDecimal>,
        balancePredicate: (BigDecimal) -> Boolean
    ): List<TopContactDto> {

        return balancesByContact.filterValues(balancePredicate).toList()
            .sortedByDescending { it.second.abs() }
            .take(5)
            .map { (contact, balance) ->
                TopContactDto(
                    id = contact.id!!,
                    fullName = contact.fullName,
                    contactType = contact.contactType,
                    amount = balance
                )
            }
    }
}
