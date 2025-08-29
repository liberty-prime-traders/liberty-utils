package me.ezrahome.libertyutils.platform.business.user_location

import me.ezrahome.libertyutils.configuration.security.LibertyPermissions
import me.ezrahome.libertyutils.configuration.session.SessionContextProvider
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import org.springframework.stereotype.Component

@Component
class UserLocationUtils(private val userLocationCache: UserLocationCache) {

    val locationPredicate: (HasLibertyLocation) -> Boolean = { locationAwareEntity ->
        LibertyPermissions.isLibertyAdmin() || locationAwareEntity.location == getUserLocation()
    }

    fun populateLocation(entity: HasLibertyLocation) {
        entity.location = getUserLocation()
    }

    private fun getUserLocation(): LibertyLocation =
        userLocationCache.findAllActiveUserLocations()
            .find { it.userId == SessionContextProvider.getUserId() }
            ?.location
            ?: throw RuntimeException("User location not found")

}
