package me.ezrahome.libertyutils.debttracker.business.summary.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class TopContactDto(
    val id: UUID,
    val name: String?,
    val amount: BigDecimal,
    val date: LocalDate?
)