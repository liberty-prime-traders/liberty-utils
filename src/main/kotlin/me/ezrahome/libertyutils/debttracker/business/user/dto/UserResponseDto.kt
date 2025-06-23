package me.ezrahome.libertyutils.debttracker.business.user.Dto

import java.io.Serializable
import java.util.UUID

/**
 * DTO for {@link me.ezra_home.debttracker.UserEntity}
 */
data class UserResponseDto(
    var id: UUID? = null,
    var FullName: String? = null,
    var Relationship: String? = null,
    var Email: String? = null,
    var PhoneNumber: String? = null
) : Serializable