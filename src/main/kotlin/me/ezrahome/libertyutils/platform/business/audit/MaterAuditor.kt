package me.ezrahome.libertyutils.platform.business.audit

import jakarta.persistence.Column
import jakarta.persistence.EntityManager
import jakarta.persistence.Table
import me.ezrahome.libertyutils.platform.configuration.session.SessionContextProvider
import me.ezrahome.libertyutils.platform.model.AuditableEntity
import me.ezrahome.libertyutils.platform.model.MasterAuditEntity
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import javax.naming.directory.InvalidAttributesException
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

@Component
class MasterAuditor(private val entityManager: EntityManager) {

    fun logInsert(entity: AuditableEntity) {
        val entityId = entity.id ?: return
        val tableName = getTableName(entity)
        val auditLogs = entity::class.memberProperties
            .filter { "id" != it.name && isFieldAuditable(it.javaField!!) }
            .map { prop ->
                val newValue = prop.getter.call(entity)?.toString() ?: "null"
                MasterAuditEntity(
                    tableName = tableName,
                    recordId = entityId,
                    fieldName = getColumnName(prop.javaField!!),
                    oldValue = "null",
                    newValue = newValue,
                    changedBy = SessionContextProvider.getUserId()
                )
            }
        persistAuditLogs(auditLogs)
    }

    private fun isFieldAuditable(field: Field): Boolean {
        return field.isAnnotationPresent(Column::class.java)
                && !field.isAnnotationPresent(Transient::class.java)
                && !field.isAnnotationPresent(AuditIgnore::class.java)
    }

    fun logUpdate(before: AuditableEntity, after: AuditableEntity) {
        val auditLogs = getAuditDiff(before, after)
        persistAuditLogs(auditLogs)
    }

   private fun getAuditDiff(before: AuditableEntity, after: AuditableEntity): List<MasterAuditEntity> {
        val entityId = before.id ?: return emptyList()
       val tableName = getTableName(before)
        return before::class.memberProperties
            .filter { "id" != it.name && isFieldAuditable(it.javaField!!) }
            .mapNotNull { prop ->
            prop.isAccessible = true
            val beforeValue = prop.getter.call(before)?.toString() ?: "null"
            val afterValue = prop.getter.call(after)?.toString() ?: "null"
            if (beforeValue != afterValue) {
                MasterAuditEntity(
                    tableName = tableName,
                    recordId = entityId,
                    fieldName = getColumnName(prop.javaField!!),
                    oldValue = beforeValue,
                    newValue = afterValue,
                    changedBy = SessionContextProvider.getUserId()
                )
            } else null
        }
    }

    private fun persistAuditLogs(auditLogs: List<MasterAuditEntity>) {
        auditLogs.forEach { log -> entityManager.persist(log) }
    }
    private fun getTableName(entity: AuditableEntity): String {
        return entity.javaClass.annotations.find { it is Table }
             ?.let { (it as Table).name }
            ?: throw InvalidAttributesException("Entity must be annotated with @Table")
    }

    private fun getColumnName(property: Field): String {
        val columnAnnotation = property.annotations.find { it is Column } as? Column
        return columnAnnotation?.name ?: throw InvalidAttributesException("Property must be annotated with @Column")
    }
}
