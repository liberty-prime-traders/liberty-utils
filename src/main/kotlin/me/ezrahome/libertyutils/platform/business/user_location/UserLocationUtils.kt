package me.ezrahome.libertyutils.platform.business.user_location

import me.ezrahome.libertyutils.configuration.security.LibertyPermissions
import me.ezrahome.libertyutils.configuration.session.SessionContextProvider
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import org.springframework.stereotype.Component

@Component
class UserLocationUtils(private val userLocationCache: UserLocationCache) {

    val locationPredicate: (HasLibertyLocation) -> Boolean = { locationAwareEntity ->
        LibertyPermissions.isLibertyAdmin() || locationAwareEntity.location == getUserLocationFromContext()
    }

    fun populateLocation(entity: HasLibertyLocation) {
        entity.location = getUserLocationFromContext()
    }

    fun getLocations(): Set<LibertyLocation> {
        if (LibertyPermissions.isLibertyAdmin()) {
            return LibertyLocation.entries.toSet()
        }
        return setOf(getUserLocationFromContext())
    }

    private fun getUserLocationFromContext(): LibertyLocation =
        userLocationCache.findAllActiveUserLocations()
            .find { it.userId == SessionContextProvider.getUserId() }
            ?.location
            ?: throw RuntimeException("User location not found")

}
