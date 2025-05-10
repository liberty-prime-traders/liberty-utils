package me.ezrahome.libertyutils.platform.business.sysuser

import me.ezrahome.libertyutils.platform.model.SysUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SysUserRepository : JpaRepository<SysUserEntity, UUID> {
}
