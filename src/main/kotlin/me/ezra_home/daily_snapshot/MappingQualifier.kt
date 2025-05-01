package me.ezra_home.daily_snapshot

import org.springframework.stereotype.Component
import java.util.Optional

@Component
object OptionalQualifier {
    fun <T> fromOptional(optional: Optional<T>): T? = optional.orElse(null)
}
