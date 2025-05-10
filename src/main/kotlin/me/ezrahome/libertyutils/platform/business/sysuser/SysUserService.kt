package me.ezrahome.libertyutils.platform.business.sysuser

import me.ezrahome.libertyutils.platform.configuration.session.SessionContextProvider
import me.ezrahome.libertyutils.platform.model.SysUserEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Objects


@Service
class SysUserService(private val sysUserCache: SysUserCache, private val sysUserMapper: SysUserMapper) {

    @Transactional
    fun addSystemUser(): SysUserDto {
        val oktaId = SessionContextProvider.getSession().oktaId
        val systemUser = sysUserCache.getSystemUsers().find { Objects.equals(oktaId, it.oktaId) }
            ?: sysUserCache.addSystemUser(SysUserEntity(oktaId))
        val oktaRecordForNewUser = sysUserCache.getUsersFromOkta().find { Objects.equals(oktaId, it.id) }
        return sysUserMapper.oktaToSystemUser(oktaRecordForNewUser) { systemUser.id }
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): Collection<SysUserDto> = sysUserCache.getAllUsers()
}
