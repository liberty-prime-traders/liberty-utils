package me.ezrahome.libertyutils.debttracker.business.contact.dto

import me.ezrahome.libertyutils.debttracker.model.ContactType
import java.io.Serializable
import java.math.BigDecimal
import java.util.UUID

/**
 * DTO for {@link me.ezra_home.debttracker.UserEntity}
 */
data class ContactResponseDto(
    var id: UUID? = null,
    var fullName: String? = null,
    var contactType: ContactType? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var balance: BigDecimal? = null
) : Serializable
