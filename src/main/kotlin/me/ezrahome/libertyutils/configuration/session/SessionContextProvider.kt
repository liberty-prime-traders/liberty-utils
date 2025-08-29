package me.ezrahome.libertyutils.configuration.session

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.UUID


object SessionContextProvider {
    private val sessionContextThreadLocal = ThreadLocal<SessionContext?>()

    fun getSession(): SessionContext {
        return sessionContextThreadLocal.get() ?: SessionContext().also { sessionContextThreadLocal.set(it) }
    }

    fun getUserId(): UUID  {
        return getSession().systemUserId ?:
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"System user id is not set")
    }

    fun clear() {
        sessionContextThreadLocal.remove()
    }
}
