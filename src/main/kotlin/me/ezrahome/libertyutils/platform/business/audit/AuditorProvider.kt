package me.ezrahome.libertyutils.platform.business.audit

import me.ezrahome.libertyutils.platform.configuration.session.SessionContextProvider
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.Optional
import java.util.UUID

@Configuration
@EnableJpaAuditing
class AuditorProvider: AuditorAware<UUID> {
    override fun getCurrentAuditor(): Optional<UUID> {
        return Optional.of(SessionContextProvider.getUserId())
    }
}
