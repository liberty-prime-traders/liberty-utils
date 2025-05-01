package me.ezra_home.daily_snapshot

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.Optional

@Component
object MappingQualifier {
    fun <T> fromOptional(optional: Optional<T>): T? = optional.orElse(null)

    fun defaultToZero(bigDecimal: BigDecimal?): BigDecimal = bigDecimal ?: BigDecimal.ZERO
}
