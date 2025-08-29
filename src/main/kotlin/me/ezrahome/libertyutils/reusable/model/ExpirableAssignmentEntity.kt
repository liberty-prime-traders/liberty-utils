package me.ezrahome.libertyutils.reusable.model

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import java.util.UUID

@MappedSuperclass
abstract class ExpirableAssignmentEntity(
    @Column(name = "user_id", updatable = false)
    var userId: UUID? = null,

    @Column(name = "start_on", updatable = false)
    @CreationTimestamp
    var startOn: OffsetDateTime? = null,

    @Column(name = "end_on")
    var endOn: OffsetDateTime? = null

): AuditableEntity() {
    fun isActive(): Boolean {
        return endOn == null
    }
}
