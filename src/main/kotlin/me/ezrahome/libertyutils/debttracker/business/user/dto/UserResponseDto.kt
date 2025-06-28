package me.ezrahome.libertyutils.debttracker.business.user.dto

import java.io.Serializable
import java.util.UUID

/**
 * DTO for {@link me.ezra_home.debttracker.UserEntity}
 */
data class UserResponseDto(
    var id: UUID? = null,
    var fullName: String? = null,
    var relationship: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null
) : Serializable