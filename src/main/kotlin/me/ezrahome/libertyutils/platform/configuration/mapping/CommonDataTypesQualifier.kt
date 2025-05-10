package me.ezrahome.libertyutils.platform.configuration.mapping

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.Optional

@Component
class CommonDataTypesQualifier {
    fun <T> fromOptional(optional: Optional<T>): T? = optional.orElse(null)

    fun fromBigDecimal(value: BigDecimal?): BigDecimal = value ?: BigDecimal.ZERO
}
