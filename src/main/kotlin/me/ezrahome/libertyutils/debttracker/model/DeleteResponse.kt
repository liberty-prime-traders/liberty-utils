package me.ezrahome.libertyutils.debttracker.model

import java.time.Instant
import java.util.UUID

class DeleteResponse (
    val id: UUID,
    val deletedOn: Instant
)