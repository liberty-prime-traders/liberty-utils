package me.ezrahome.libertyutils.platform.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import me.ezrahome.libertyutils.platform.constants.TableNames
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedBy
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = TableNames.MASTER_AUDIT)
class MasterAuditEntity(
    @NotNull
    @Column(name = "table_name", nullable = false)
    var tableName: String,

    @NotNull
    @Column(name = "record_id", nullable = false)
    var recordId: UUID,

    @Size(max = 200)
    @NotNull
    @Column(name = "field_name", nullable = false, length = 200)
    var fieldName: String,

    @NotNull
    @Column(name = "old_value", nullable = false, length = Integer.MAX_VALUE)
    var oldValue: String,

    @NotNull
    @Column(name = "new_value", nullable = false, length = Integer.MAX_VALUE)
    var newValue: String,

    @NotNull
    @CreatedBy
    @Column(name = "changed_by", nullable = false)
    var changedBy: UUID,

    @NotNull
    @CreationTimestamp
    @Column(name = "changed_on", nullable = false)
    var changedOn: OffsetDateTime? = null

): BaseEntity()
