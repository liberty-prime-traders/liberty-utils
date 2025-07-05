package me.ezrahome.libertyutils.debttracker.business.contact.dto

import me.ezrahome.libertyutils.debttracker.model.ContactType
import java.io.Serializable
import java.util.Optional
import java.util.UUID

data class ContactUpdateDto(
    var id: UUID? = null,
    var fullName: Optional<String>? = null,
    var contactType: Optional<ContactType>? = null,
    var email: Optional<String>? = null,
    var phoneNumber: Optional<String>? = null
) : Serializable