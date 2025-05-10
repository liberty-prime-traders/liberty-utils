package me.ezrahome.libertyutils.platform.configuration.mapping


import me.ezrahome.libertyutils.platform.business.sysuser.SysUserCache
import org.springframework.stereotype.Component
import java.util.Objects
import java.util.UUID
import kotlin.collections.find

@Component
class UserQualifier(private val sysUserCache: SysUserCache, ) {

    @FullName
    fun getUserFullName(userId: UUID?): String? {
        if (userId == null) return null
        val userDto = sysUserCache.getAllUsers().find { Objects.equals(userId, it.id) }
        if (userDto == null) return null
        return "${userDto.firstName} ${userDto.lastName}"
    }
}
