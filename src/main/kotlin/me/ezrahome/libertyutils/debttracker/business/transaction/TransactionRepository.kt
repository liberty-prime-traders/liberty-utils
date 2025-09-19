package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
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

    fun findByTransactionDateBetween(transactionDateAfter: LocalDate, transactionDateBefore: LocalDate): List<TransactionEntity>

    fun findByTransactionDateGreaterThanEqualAndTransactionDateLessThanEqual(
        transactionDateAfter: LocalDate,
        transactionDateBefore: LocalDate
    ): List<TransactionEntity>

    fun findByTransactionDateInAndLocationIn(dates: Set<LocalDate>, locations: Set<LibertyLocation>): List<TransactionEntity>

    @Query(
        "select t.userId as userId, " +
                "sum(" +
                "   case " +
                "       when t.transactionType = me.ezrahome.libertyutils.debttracker.model.TransactionType.DEBIT then -t.amount " +
                "       else t.amount " +
                "   end" +
                ") as balance " +
            "from TransactionEntity t where t.location in :locations group by t.userId"
    )
    fun findAllUserBalancesByLocations(locations: Set<LibertyLocation>): List<UserBalanceProjection>

    fun findByLocationInOrderByTransactionDateDesc(locations: Set<LibertyLocation>, pageable: Pageable): List<TransactionEntity>
}
