package me.ezrahome.libertyutils.debttracker.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import me.ezrahome.libertyutils.platform.constants.TableNames
import me.ezrahome.libertyutils.platform.model.AuditableEntity
import org.hibernate.annotations.ColumnDefault
import java.util.UUID
import java.time.LocalDate
import java.math.BigDecimal

@Entity
@Table(name = TableNames.TRANSACTION)
class TransactionEntity(
    @NotNull
    @Column(name = "user_id", nullable = false)
    var UserID: UUID? = null,

    @Column(name = "transaction_date", updatable = false)
    var TransactionDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    var TransactionType: TransactionTypes? = null,

    @ColumnDefault("0")
    @Column(name = "amount")
    var Amount: BigDecimal? = null,

    @Column(name = "description")
    var Description: String? = null

) : AuditableEntity()