package me.ezrahome.libertyutils.dailysnapshot.business.user_location

import me.ezrahome.libertyutils.platform.business.sysuser.SysUserCache
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class UserLocationService(
    private val userLocationMapper: UserLocationMapper,
    private val userLocationRepository: UserLocationRepository,
    private val sysUserCache: SysUserCache
) {

    fun getUserLocations(): Collection<UserLocationResponseDto> {
        return userLocationRepository.findAll()
            .map { userLocationMapper.toResponseDto(it) }
    }

    fun addUserToLocation(userLocationInsertDto: UserLocationInsertDto): Collection<UserLocationResponseDto> {
        validateInsertDto(userLocationInsertDto)
        val userLocationEntity = userLocationMapper.toEntity(userLocationInsertDto)
        userLocationRepository.findByUserIdAndEndOnNull(userLocationInsertDto.userId!!)
            ?.apply { this.endOn = OffsetDateTime.now() }
            ?.let { userLocationRepository.save(it) }
        userLocationRepository.save(userLocationEntity)
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
