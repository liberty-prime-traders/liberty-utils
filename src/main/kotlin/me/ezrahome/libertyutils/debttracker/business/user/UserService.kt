package me.ezrahome.libertyutils.debttracker.business.user

import me.ezrahome.libertyutils.debttracker.business.user.dto.UserInsertDto
import me.ezrahome.libertyutils.debttracker.business.user.dto.UserUpdateDto
import me.ezrahome.libertyutils.debttracker.business.user.dto.UserResponseDto
import me.ezrahome.libertyutils.debttracker.model.UserEntity
import me.ezrahome.libertyutils.platform.utils.StringUtils
import org.springframework.stereotype.Service
import java.util.Objects
import java.util.UUID


@Service
class UserService(
    private val userMapper: UserMapper,
    private val userCache: UserCache
) {

    fun getAllUsers(): Collection<UserResponseDto> {
        return userCache.getAllUsers().map { userMapper.toResponseDto(it) }
    }

    fun createUser(userInsertDto: UserInsertDto): UserResponseDto {
        val value = StringUtils.getValueOrException(userInsertDto.fullName, NAME_IS_REQUIRED)
        userCache.getAllUsers().find { StringUtils.isEquivalent(it.fullName, value) }
            ?.let { throw RuntimeException(String.format(NAME_ALREADY_EXISTS, value)) }

        val newUserEntity = userMapper.toEntity(userInsertDto)
        userCache.upsertUser(newUserEntity)
        return userMapper.toResponseDto(newUserEntity)
    }

    fun updateUser(userDto: UserUpdateDto): UserResponseDto {
        validateUserUpdate(userDto)
        val userToUpdate = userCache.getAllUsers().find { Objects.equals(userDto.id, it.id) }
            ?: throw RuntimeException("User not found")
        userMapper.partialUpdate(userDto, userToUpdate)
        userCache.upsertUser(userToUpdate)
        return userMapper.toResponseDto(userToUpdate)
    }

    private fun validateUserUpdate(userUpdateDto: UserUpdateDto) {
        val value = StringUtils.getValueOrException(userUpdateDto.fullName, NAME_IS_REQUIRED)
        userCache.getAllUsers().find { it.fullName.equals(value, ignoreCase = true) && it.id != userUpdateDto.id }
            ?.let{ throw RuntimeException(String.format(NAME_ALREADY_EXISTS, value)) }
    }

    fun deleteUser(id: UUID?) {
        userCache.deleteUser(id!!)
    }

    companion object {
        const val NAME_IS_REQUIRED = "A person must have a name"
        const val NAME_ALREADY_EXISTS = "A person with the name %s already exists."
    }

}