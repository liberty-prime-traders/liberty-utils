package me.ezrahome.libertyutils.dailysnapshot.business.user_location

import me.ezrahome.libertyutils.dailysnapshot.model.LibertyLocation
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.UUID

data class UserLocationResponseDto(
    var id: UUID? = null,
    var userName: String? = null,
    var location: LibertyLocation? = null,
    var startOn: OffsetDateTime? = null,
    var endOn: OffsetDateTime? = null
): Serializable
