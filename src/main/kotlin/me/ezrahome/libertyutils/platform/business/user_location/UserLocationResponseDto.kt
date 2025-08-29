package me.ezrahome.libertyutils.platform.business.user_location

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import me.ezrahome.libertyutils.configuration.serializer.DatesToMillis
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.UUID

data class UserLocationResponseDto(
    var id: UUID? = null,
    var userName: String? = null,
    var location: LibertyLocation? = null,

    @get:JsonSerialize(using = DatesToMillis::class)
    var startOn: OffsetDateTime? = null,

    @get:JsonSerialize(using = DatesToMillis::class)
    var endOn: OffsetDateTime? = null
): Serializable
