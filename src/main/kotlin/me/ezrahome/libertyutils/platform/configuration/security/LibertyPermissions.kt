package me.ezrahome.libertyutils.platform.configuration.security

import org.springframework.security.core.context.SecurityContextHolder


object LibertyPermissions {

    fun isLibertyAdmin(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.authorities.any { it.authority == "ROLE_${LibertyRoles.ROLE_LIBERTY_ADMIN}"}
    }
}
