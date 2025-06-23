package me.ezrahome.libertyutils.debttracker.business.user.Dto

import java.io.Serializable

data class UserInsertDto(
    val FullName: String? = null,
    val Relationship: String? = null,
    val Email: String? null,
    val PhoneNumber: String? null,
): Serializable