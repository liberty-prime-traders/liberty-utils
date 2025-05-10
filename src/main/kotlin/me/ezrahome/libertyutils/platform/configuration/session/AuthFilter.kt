package me.ezrahome.libertyutils.platform.configuration.session

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.ezrahome.libertyutils.platform.business.sysuser.SysUserCache
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthFilter(private val sysUserCache: SysUserCache,): OncePerRequestFilter() {

    companion object {
        private const val OKTA_ID_KEY = "uid"
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (request.method.equals("OPTIONS", ignoreCase = true)) {
            chain.doFilter(request, response)
            return
        }
        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as Jwt
        val sessionContext = SessionContextProvider.getSession()
        sessionContext.oktaId = jwt.claims[OKTA_ID_KEY] as String
        initializeSystemUserId(sessionContext)

        try {
            chain.doFilter(request, response)
        } finally {
            SessionContextProvider.clear()
        }
    }

    private fun initializeSystemUserId(sessionContext: SessionContext) {
        sysUserCache.getAllUsers().find { it.oktaId == sessionContext.oktaId }
            ?.let { sessionContext.systemUserId = it.id }
    }
}


