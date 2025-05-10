package me.ezrahome.libertyutils.dailysnapshot

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import me.ezrahome.libertyutils.platform.business.audit.AuditIgnore
import me.ezrahome.libertyutils.platform.business.audit.UiLabel
import me.ezrahome.libertyutils.platform.constants.TableNames
import me.ezrahome.libertyutils.platform.model.AuditableEntity
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
    @UiLabel("Starting Balance")
    var startBalanceCash: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "end_balance_cash")
    @UiLabel("Ending Balance")
    var endBalanceCash: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "outflow_cash")
    @UiLabel("Cash Outflow")
    var outflowCash: BigDecimal? = null,

    @Column(name = "cogs")
    @UiLabel("COGS")
    var cogs: BigDecimal? = null,

    @Column(name = "cogs_returned")
    @UiLabel("COGS Returned")
    var cogsReturned: BigDecimal? = null,

    @Column(name = "expenses")
    @UiLabel("Expenses")
    var expenses: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "inflow_joint_account")
    @UiLabel("Joint Account Inflow")
    var inflowJointAccount: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "inflow_personal_account")
    @UiLabel("Personal Account Inflow")
    var inflowPersonalAccount: BigDecimal? = null,

    @ColumnDefault("0")
    @Column(name = "inflow_credit_sales")
    @UiLabel("Credit Sales")
    var inflowCreditSales: BigDecimal? = null,
): AuditableEntity()
