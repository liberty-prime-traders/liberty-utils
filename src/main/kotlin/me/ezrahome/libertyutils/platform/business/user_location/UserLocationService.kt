package me.ezrahome.libertyutils.platform.business.user_location

import me.ezrahome.libertyutils.platform.business.sysuser.SysUserCache
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class UserLocationService(
    private val userLocationMapper: UserLocationMapper,
    private val userLocationCache: UserLocationCache,
    private val sysUserCache: SysUserCache
) {

    fun getUserLocations(): Collection<UserLocationResponseDto> {
        return userLocationCache.findAll()
            .map { userLocationMapper.toResponseDto(it) }
    }

    fun addUserToLocation(userLocationInsertDto: UserLocationInsertDto): Collection<UserLocationResponseDto> {
        validateInsertDto(userLocationInsertDto)
        val userLocationEntity = userLocationMapper.toEntity(userLocationInsertDto)
        userLocationCache.findAllActiveUserLocations()
            .find { it.userId == userLocationInsertDto.userId }
            ?.apply { this.endOn = OffsetDateTime.now() }
            ?.let { userLocationCache.save(it) }
        userLocationCache.save(userLocationEntity)
        return getUserLocations()
    }

    private fun validateInsertDto(userLocationInsertDto: UserLocationInsertDto) {
        if (userLocationInsertDto.userId == null) {
            throw IllegalArgumentException("User ID must be provided")
        }
        sysUserCache.getAllUsers().find { it.id == userLocationInsertDto.userId }
            ?: throw IllegalArgumentException("User with ID ${userLocationInsertDto.userId} not found")
    }

}
