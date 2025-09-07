package me.ezrahome.libertyutils.debttracker.business.contact.mapping

import me.ezrahome.libertyutils.debttracker.business.contact.ContactNetStandingCache
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class ContactBalanceQualifier(private val contactNetStandingCache: ContactNetStandingCache) {

    @ContactBalance
    fun getContactBalance(userId: UUID?): BigDecimal = contactNetStandingCache.getNetStanding(userId)
}
