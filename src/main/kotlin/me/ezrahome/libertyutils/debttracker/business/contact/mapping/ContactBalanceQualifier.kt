package me.ezrahome.libertyutils.debttracker.business.contact.mapping

import me.ezrahome.libertyutils.debttracker.business.contact.ContactBalanceCache
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class ContactBalanceQualifier(private val contactBalanceCache: ContactBalanceCache) {

    @ContactBalance
    fun getContactBalance(userId: UUID?): BigDecimal = contactBalanceCache.getBalance(userId)
}
