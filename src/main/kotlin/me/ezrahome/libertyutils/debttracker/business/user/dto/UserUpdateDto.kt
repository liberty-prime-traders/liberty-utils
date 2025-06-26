package me.ezrahome.libertyutils.debttracker.business.user.dto

import java.io.Serializable
import java.util.Optional
import java.util.UUID

data class UserUpdateDto(
    var id: UUID? = null,
    var fullName: Optional<String>? = null,
    var relationship: Optional<String>? = null,
    var email: Optional<String>? = null,
    var phoneNumber: Optional<String>? = null
) : Serializable