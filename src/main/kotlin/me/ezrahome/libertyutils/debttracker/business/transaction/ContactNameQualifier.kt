package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.debttracker.business.contact.ContactCache
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ContactNameQualifier(private val contactCache: ContactCache) {
    @ContactName
    fun getContactName(userID: UUID?):String? {
        return userID.let {
            contactCache.getAllContacts().find {
                it.id == userID
            }?.fullName
        }
    }
}