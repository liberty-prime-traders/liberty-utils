package me.ezrahome.libertyutils.platform.business.sysuser

import com.okta.sdk.client.Client
import com.okta.sdk.resource.user.UserList
import me.ezrahome.libertyutils.configuration.cache.CacheNames
import me.ezrahome.libertyutils.platform.model.SysUserEntity
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Collections
import kotlin.collections.map


@Service
@CacheConfig(cacheNames = [CacheNames.SYS_USER])
class SysUserCache(
    private val userRepository: SysUserRepository,
    private val sysUserMapper: SysUserMapper,
    private val oktaClient: Client
) {

    @Cacheable
    fun getSystemUsers(): Collection<SysUserEntity> {
        return userRepository.findAll()
    }

    @Cacheable
    fun getUsersFromOkta(): UserList {
        try {
            return oktaClient.listUsers()
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch users from Okta", e)
        }
    }

    @Cacheable
    fun getAllUsers(): Collection<SysUserDto> {
        val sysUsers = getSystemUsers()
        if (sysUsers.isEmpty()) return Collections.emptyList()
        val oktaUsers = getUsersFromOkta().associateBy { it.id }
        return sysUsers.map { sysUser ->
            val oktaUser = oktaUsers[sysUser.oktaId]
            sysUserMapper.oktaToSystemUser(oktaUser) {sysUser.id}
        }
    }

    @CacheEvict(allEntries = true)
    fun addSystemUser(userEntity: SysUserEntity): SysUserEntity = userRepository.save(userEntity)
}
