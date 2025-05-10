package me.ezrahome.libertyutils.platform.configuration.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.OffsetDateTime

class DatesToMillis : JsonSerializer<OffsetDateTime>() {
    override fun serialize(value: OffsetDateTime, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeNumber(value.toInstant().toEpochMilli())
    }
}
