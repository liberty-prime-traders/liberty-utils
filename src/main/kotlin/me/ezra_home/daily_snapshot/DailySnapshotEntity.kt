package me.ezra_home.daily_snapshot

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "daily_snapshot")
class DailySnapshotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    @Column(name = "snapshot_date")
    var snapshotDate: LocalDate? = null

    @ColumnDefault("now()")
    @Column(name = "created_on")
    @CreationTimestamp
    var createdOn: Instant? = null

    @ColumnDefault("0")
    @Column(name = "start_balance_cash")
    var startBalanceCash: BigDecimal? = null

    @ColumnDefault("0")
    @Column(name = "end_balance_cash")
    var endBalanceCash: BigDecimal? = null

    @ColumnDefault("0")
    @Column(name = "outflow_cash")
    var outflowCash: BigDecimal? = null

    @Column(name = "cogs")
    var cogs: BigDecimal? = null

    @Column(name = "cogs_returned")
    var cogsReturned: BigDecimal? = null

    @Column(name = "expenses")
    var expenses: BigDecimal? = null

    @ColumnDefault("0")
    @Column(name = "inflow_joint_account")
    var inflowJointAccount: BigDecimal? = null

    @ColumnDefault("0")
    @Column(name = "inflow_personal_account")
    var inflowPersonalAccount: BigDecimal? = null

    @ColumnDefault("0")
    @Column(name = "inflow_credit_sales")
    var inflowCreditSales: BigDecimal? = null
}
