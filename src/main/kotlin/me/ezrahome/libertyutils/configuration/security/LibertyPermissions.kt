package me.ezrahome.libertyutils.configuration.security

import org.springframework.security.core.context.SecurityContextHolder


object LibertyPermissions {

    fun isLibertyAdmin(): Boolean {
        val disableAuth = System.getenv("DISABLE_AUTH")?.equals("true", ignoreCase = true) == true
        if (disableAuth) return true
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication != null && authentication.authorities.any { it.authority == "ROLE_${LibertyRoles.ROLE_LIBERTY_ADMIN}"}
    }
}
