package me.ezrahome.libertyutils.platform.business.audit

import me.ezrahome.libertyutils.platform.model.MasterAuditEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MasterAuditRepository: JpaRepository<MasterAuditEntity, UUID> {
    fun findByTableNameAndRecordId(tableName: String, recordId: UUID): Collection<MasterAuditEntity>
}
