package me.ezrahome.libertyutils.debttracker.business.user.dto

import java.io.Serializable

data class UserInsertDto(
    val fullName: String? = null,
    val relationship: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null
): Serializable