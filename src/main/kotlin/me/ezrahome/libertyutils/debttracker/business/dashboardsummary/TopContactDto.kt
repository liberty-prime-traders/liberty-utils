package me.ezrahome.libertyutils.debttracker.business.dashboardsummary

import me.ezrahome.libertyutils.debttracker.model.ContactType
import java.math.BigDecimal
import java.util.UUID

data class TopContactDto(
    val id: UUID,
    val fullName: String?,
    val contactType: ContactType?,
    val amount: BigDecimal
)
