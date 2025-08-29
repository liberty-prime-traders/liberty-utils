package me.ezrahome.libertyutils.debttracker.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import me.ezrahome.libertyutils.platform.business.user_location.HasLibertyLocation
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import me.ezrahome.libertyutils.reusable.constants.TableNames
import me.ezrahome.libertyutils.reusable.model.AuditableEntity
import org.hibernate.annotations.ColumnDefault
import java.util.UUID
import java.time.LocalDate
import java.math.BigDecimal

@Entity
@Table(name = TableNames.TRANSACTION)
class TransactionEntity(
    @NotNull
    @Column(name = "user_id", nullable = false)
    var userId: UUID? = null,

    @Column(name = "transaction_date", updatable = false)
    var transactionDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    var transactionType: TransactionType? = null,

    @ColumnDefault("0")
    @Column(name = "amount")
    var amount: BigDecimal? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "location", updatable = false)
    @Enumerated(EnumType.STRING)
    override var location: LibertyLocation? = null

) : AuditableEntity(), HasLibertyLocation
