package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.debttracker.business.summary.dto.LatestTransactionsDto
import me.ezrahome.libertyutils.debttracker.business.summary.dto.StatsDto
import me.ezrahome.libertyutils.debttracker.business.summary.dto.TopUserDto
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

interface UserBalanceProjection {
    val userId: UUID
    val balance: BigDecimal?
}

@Repository
interface TransactionRepository: JpaRepository<TransactionEntity, UUID> {
    fun findTransactionsByTransactionDateBetween(transactionDateAfter: LocalDate, transactionDateBefore: LocalDate): List<TransactionEntity>

    @Query(
        "select t.userId as userId, " +
                "sum(" +
                "   case " +
                "       when t.transactionType = me.ezrahome.libertyutils.debttracker.model.TransactionType.DEBIT then t.amount " +
                "       else -t.amount " +
                "   end" +
                ") as balance " +
            "from TransactionEntity t group by t.userId"
    )
    fun findAllUserBalances(): List<UserBalanceProjection>

   @Query("SELECT new me.ezrahome.libertyutils.debttracker.business.summary.dto.LatestTransactionsDto(" +
            "        t.id, " +
            "        t.amount, " +
            "        t.transactionDate, " +
            "        t.description, " +
            "        t.transactionType, " +
            "        c.fullName" +
            "    )" +
            "    FROM TransactionEntity t" +
            "    JOIN ContactEntity c ON t.userId = c.id" +
            "    ORDER BY t.transactionDate DESC" )
    fun findLatestTransactions(pageable: Pageable): List<LatestTransactionsDto>

    @Query("""
        SELECT new me.ezrahome.libertyutils.debttracker.business.summary.dto.StatsDto (
            COALESCE(SUM(CASE WHEN t.transactionType = me.ezrahome.libertyutils.debttracker.model.TransactionType.DEBIT THEN t.amount ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.transactionType = me.ezrahome.libertyutils.debttracker.model.TransactionType.CREDIT THEN t.amount ELSE 0 END), 0),
            COUNT(DISTINCT CASE WHEN t.transactionType = me.ezrahome.libertyutils.debttracker.model.TransactionType.DEBIT THEN t.userId ELSE NULL END),
            COUNT(DISTINCT CASE WHEN t.transactionType = me.ezrahome.libertyutils.debttracker.model.TransactionType.CREDIT THEN t.userId ELSE NULL END)
        )
        FROM TransactionEntity t
    """)
    fun getSummaryStats(): StatsDto

    @Query("""
        SELECT new me.ezrahome.libertyutils.debttracker.business.summary.dto.TopUserDto (
            t.userId,
            c.fullName,
            SUM(t.amount),
            MAX(t.transactionDate)
        )
        FROM TransactionEntity t
        LEFT JOIN ContactEntity c ON t.userId = c.id
        WHERE t.transactionType = :type
        GROUP BY t.userId, c.fullName
        ORDER BY SUM(t.amount) DESC
    """)
    fun findTopUsersByType(type: TransactionType, pageable: Pageable): List<TopUserDto>
}
