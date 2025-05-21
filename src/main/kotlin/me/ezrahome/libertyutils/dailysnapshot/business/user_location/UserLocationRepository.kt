package me.ezrahome.libertyutils.dailysnapshot.business.user_location

import me.ezrahome.libertyutils.dailysnapshot.model.UserLocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserLocationRepository: JpaRepository<UserLocationEntity, UUID> {
    fun findByUserIdAndEndOnNull(userId: UUID): UserLocationEntity?
}
