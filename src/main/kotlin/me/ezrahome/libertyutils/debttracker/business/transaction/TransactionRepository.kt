package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.UUID

interface UserBalanceProjection {
    val userId: UUID
    val balance: BigDecimal?
}

@Repository
interface TransactionRepository: JpaRepository<TransactionEntity, UUID> {

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
}
