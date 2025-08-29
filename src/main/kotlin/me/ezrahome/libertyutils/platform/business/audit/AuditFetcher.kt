package me.ezrahome.libertyutils.platform.business.audit

import jakarta.persistence.Column
import me.ezrahome.libertyutils.configuration.mapping.UserQualifier
import me.ezrahome.libertyutils.reusable.model.AuditableEntity
import org.springframework.stereotype.Component
import java.io.InvalidObjectException
import java.util.UUID

@Component
class AuditFetcher(
    private val masterAuditRepository: MasterAuditRepository,
    private val userQualifier: UserQualifier
) {

    fun <T: AuditableEntity> getAuditRecords(tableName: String, recordId: UUID, tableClass: Class<T>): Collection<MasterAuditDto> {
        val auditRecords = masterAuditRepository.findByTableNameAndRecordId(tableName, recordId)
        return auditRecords.map { auditRecord ->
            MasterAuditDto(
                id = auditRecord.id,
                changedBy = userQualifier.getUserFullName(auditRecord.changedBy),
                changedOn = auditRecord.changedOn,
                fieldName = getFieldName(tableClass, auditRecord.fieldName),
                oldValue = auditRecord.oldValue,
                newValue = auditRecord.newValue
            )
        }
    }

    private fun <T: AuditableEntity> getFieldName(tableClass: Class<T>, columnName: String): String {
        return tableClass.declaredFields
            .find { field -> field.getAnnotation(Column::class.java)?.name == columnName }?.name
            ?: throw InvalidObjectException("Column name '$columnName' does not match any field name")
    }

}
