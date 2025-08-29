package me.ezrahome.libertyutils.platform.business.user_location

import me.ezrahome.libertyutils.platform.model.UserLocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserLocationRepository: JpaRepository<UserLocationEntity, UUID> {
    fun findByEndOnNull(): Collection<UserLocationEntity>
}
