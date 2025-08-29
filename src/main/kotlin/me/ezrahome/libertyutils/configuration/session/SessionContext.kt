package me.ezrahome.libertyutils.configuration.session

import java.util.UUID


data class SessionContext(
    var oktaId: String? = null,
    var systemUserId: UUID? = null
)
