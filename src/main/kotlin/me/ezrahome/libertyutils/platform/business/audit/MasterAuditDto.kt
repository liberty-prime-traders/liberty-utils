package me.ezrahome.libertyutils.platform.business.audit

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import me.ezrahome.libertyutils.configuration.serializer.DatesToMillis
import java.time.OffsetDateTime
import java.util.UUID

data class MasterAuditDto(
    val id: UUID?,
    val changedBy: String?,
    @field:JsonSerialize(using = DatesToMillis::class)
    val changedOn: OffsetDateTime?,
    val fieldName: String?,
    val oldValue: String?,
    val newValue: String?
)
