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
        return userCache.getAllUsers().map { userMapper.toDto(it) }
    }

    fun createUser(userInsertDto: UserInsertDto): UserResponseDto {
        val value = StringUtils.getValueOrException(userInsertDto.value, NAME_IS_REQUIRED)
        userCache.getAllUsers().find { StringUtils.isEquivalent(it.value, value) }
            ?.let { throw RuntimeException(String.format(NAME_ALREADY_EXISTS, value)) }

        val newUserEntity = userMapper.toEntity(userInsertDto)
        userCache.upsertUser(newUserEntity)
        return userMapper.toDto(newUserEntity)
    }

    fun updateUser(userDto: UserUpdateDto): UserResponseDto {
        validateUserUpdate(userDto)
        val userToUpdate = userCache.getAllUsers().find { Objects.equals(userDto.id, it.id) }
            ?: throw UpdatingNonExistingRecordException()
        userMapper.partialUpdate(userDto, userToUpdate)
        userCache.upsertUser(userToUpdate)
        return userMapper.toDto(userToUpdate)
    }

    private fun validateUserUpdate(userUpdateDto: UserUpdateDto) {
        val value = StringUtils.getValueOrException(userUpdateDto.value, NAME_IS_REQUIRED)
        userCache.getAllUsers().find { it.value.equals(value, ignoreCase = true) && it.id != userUpdateDto.id }
            ?.let{ throw RuntimeException(String.format(NAME_ALREADY_EXISTS, value)) }
    }

    fun deleteUser(id: UUID?) {
        userCache.deleteUser(id)
    }

    companion object {
        const val NAME_IS_REQUIRED = "A person must have a name"
        const val NAME_ALREADY_EXISTS = "A person with the name %s already exists."
    }

}