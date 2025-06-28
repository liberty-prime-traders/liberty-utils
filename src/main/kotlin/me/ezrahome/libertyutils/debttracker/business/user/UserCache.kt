package me.ezrahome.libertyutils.debttracker.business.user

import me.ezrahome.libertyutils.platform.configuration.cache.CacheNames
import me.ezrahome.libertyutils.debttracker.model.UserEntity
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@CacheConfig(cacheNames = [CacheNames.USER])
class UserCache(
    private val userRepository: UserRepository,
) {

    @Cacheable
    fun getAllUsers(): Collection<UserEntity> = userRepository.findAll()

    @CacheEvict(allEntries = true)
    fun upsertUser(userEntity: UserEntity): UserEntity = userRepository.save(userEntity)

    @CacheEvict(allEntries = true)
    fun deleteUser(id: UUID) {
        userRepository.deleteById(id)
    }
}
