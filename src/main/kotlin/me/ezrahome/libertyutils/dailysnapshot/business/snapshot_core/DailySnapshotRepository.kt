package me.ezrahome.libertyutils.dailysnapshot.business.snapshot_core

import me.ezrahome.libertyutils.dailysnapshot.model.DailySnapshotEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface DailySnapshotRepository: JpaRepository<DailySnapshotEntity, UUID> {
    fun findBySnapshotDateBetween(snapshotDateAfter: LocalDate, snapshotDateBefore: LocalDate): List<DailySnapshotEntity>
}
