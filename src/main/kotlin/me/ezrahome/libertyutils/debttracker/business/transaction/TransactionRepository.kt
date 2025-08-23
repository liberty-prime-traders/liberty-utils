package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface TransactionRepository: JpaRepository<TransactionEntity, UUID> {
    fun findTransactionsByTransactionDateBetween(transactionDateAfter: LocalDate, transactionDateBefore: LocalDate): List<TransactionEntity>
}
