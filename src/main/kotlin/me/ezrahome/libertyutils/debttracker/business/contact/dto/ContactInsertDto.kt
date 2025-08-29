package me.ezrahome.libertyutils.debttracker.business.contact.dto

import me.ezrahome.libertyutils.debttracker.model.ContactType
import java.io.Serializable

data class ContactInsertDto(
    val fullName: String? = null,
    val contactType: ContactType? = null,
    val email: String? = null,
    val phoneNumber: String? = null
): Serializable