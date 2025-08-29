package me.ezrahome.libertyutils.platform.business.user_location

import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import java.io.Serializable
import java.util.UUID

data class UserLocationInsertDto(
    val userId: UUID? = null,
    val location: LibertyLocation? = null
): Serializable
