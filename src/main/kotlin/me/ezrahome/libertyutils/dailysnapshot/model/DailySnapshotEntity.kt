package me.ezrahome.libertyutils.dailysnapshot.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import me.ezrahome.libertyutils.platform.business.audit.AuditIgnore
import me.ezrahome.libertyutils.platform.business.user_location.HasLibertyLocation
import me.ezrahome.libertyutils.reusable.constants.TableNames
import me.ezrahome.libertyutils.reusable.model.AuditableEntity
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = TableNames.DAILY_SNAPSHOT)
class DailySnapshotEntity(

    @Column(name = "snapshot_date", updatable = false)
    var snapshotDate: LocalDate? = null,

    @ColumnDefault("now()")
    @Column(name = "created_on")
    @CreationTimestamp
    @AuditIgnore
    var createdOn: Instant? = null,

    @ColumnDefault("0")
    @Column(name = "start_balance_cash")
    var startBalanceCash: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "end_balance_cash")
    var endBalanceCash: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "outflow_cash")
    var outflowCash: BigDecimal? = null,

    @Column(name = "cogs")
    var cogs: BigDecimal? = null,

    @Column(name = "cogs_returned")
    var cogsReturned: BigDecimal? = null,

    @Column(name = "expenses")
    var expenses: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "inflow_joint_account")
    var inflowJointAccount: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "inflow_personal_account")
    var inflowPersonalAccount: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "inflow_credit_sales")
    var inflowCreditSales: BigDecimal? = null,

    @Column(name = "location", updatable = false)
    @Enumerated(EnumType.STRING)
    override var location: LibertyLocation? = null,

    @ColumnDefault("0")
    @Column(name = "transaction_costs")
    var transactionCosts: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "relay_sales")
    var relaySales: BigDecimal? = null

): AuditableEntity(), HasLibertyLocation
