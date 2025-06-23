package me.ezrahome.libertyutils.debttracker.business.user.Dto

import java.io.Serializable
import java.util.Optional
import java.util.UUID

data class UserUpdateDto(
    var id: UUID? = null,
    var FullName: Optional<String>? = null,
    var Relationship: Optional<String>? = null,
    var Email: Optional<String>? = null,
    var PhoneNumber: Optional<String>? = null
) : Serializable