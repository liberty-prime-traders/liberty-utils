package me.ezrahome.libertyutils.platform.rest.sysuser

import me.ezrahome.libertyutils.configuration.security.LibertyRoles
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationInsertDto
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationResponseDto
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("secured/user-location")
@PreAuthorize("hasRole('${LibertyRoles.ROLE_LIBERTY_ADMIN}')")
class UserLocationEndpoint(private val userLocationService: UserLocationService) {

    @GetMapping
    fun getUserLocations(): Collection<UserLocationResponseDto> {
        return userLocationService.getUserLocations()
    }

    @PostMapping
    fun addUserToLocation(@RequestBody userLocationInsertDto: UserLocationInsertDto): Collection<UserLocationResponseDto> {
        return userLocationService.addUserToLocation(userLocationInsertDto)
    }

}
