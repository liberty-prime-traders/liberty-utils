package me.ezra_home.daily_snapshot

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface DailySnapshotRepository: JpaRepository<DailySnapshotEntity, UUID> {
    fun findBySnapshotDateBetween(snapshotDateAfter: LocalDate, snapshotDateBefore: LocalDate): List<DailySnapshotEntity>
}
