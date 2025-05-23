package me.ezrahome.libertyutils.dailysnapshot.business.user_location

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import me.ezrahome.libertyutils.dailysnapshot.model.LibertyLocation
import me.ezrahome.libertyutils.platform.configuration.serializer.DatesToMillis
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.UUID

data class UserLocationResponseDto(
    var id: UUID? = null,
    var userName: String? = null,
    var location: LibertyLocation? = null,

    @JsonSerialize(using = DatesToMillis::class)
    var startOn: OffsetDateTime? = null,

    @JsonSerialize(using = DatesToMillis::class)
    var endOn: OffsetDateTime? = null
): Serializable
