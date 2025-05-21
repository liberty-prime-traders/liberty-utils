package me.ezrahome.libertyutils.dailysnapshot.business.user_location

import me.ezrahome.libertyutils.dailysnapshot.model.LibertyLocation
import java.io.Serializable
import java.util.UUID

data class UserLocationInsertDto(
    val userId: UUID? = null,
    val location: LibertyLocation? = null
): Serializable
