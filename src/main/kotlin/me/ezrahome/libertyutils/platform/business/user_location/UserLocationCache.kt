package me.ezrahome.libertyutils.platform.business.user_location

import me.ezrahome.libertyutils.configuration.cache.CacheNames
import me.ezrahome.libertyutils.platform.model.UserLocationEntity
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = [CacheNames.USER_LOCATION])
class UserLocationCache(private val userLocationRepository: UserLocationRepository) {

    @Cacheable
    fun findAllActiveUserLocations(): Collection<UserLocationEntity> =
        userLocationRepository.findByEndOnNull()

    @Cacheable
    fun findAll(): Collection<UserLocationEntity> = userLocationRepository.findAll()

    @CacheEvict(allEntries = true)
    fun save(userLocationEntity: UserLocationEntity) =
        userLocationRepository.save(userLocationEntity)
}
