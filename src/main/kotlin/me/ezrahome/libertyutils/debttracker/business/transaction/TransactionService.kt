package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.configuration.security.LibertyPermissions
import me.ezrahome.libertyutils.debttracker.business.contact.ContactCache
import me.ezrahome.libertyutils.debttracker.business.contact.ContactNetStandingCache
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import me.ezrahome.libertyutils.debttracker.business.transaction.mapping.TransactionMapper
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class TransactionService(
    private val transactionMapper: TransactionMapper,
    private val transactionCache: TransactionCache,
    private val contactCache: ContactCache,
    private val userLocationUtils: UserLocationUtils,
    private val contactNetStandingCache: ContactNetStandingCache,
    private val transactionRepository: TransactionRepository,
    private val dailyDebtSummaryCache: me.ezrahome.libertyutils.debttracker.business.dailysummary.DailyDebtSummaryCache
) {

    @Transactional(readOnly = true)
    fun getTransactionsForTransactionDate(startDate: String, endDate: String): Collection<TransactionResponseDto> {
        val transactionDateAfter = java.time.LocalDate.parse(startDate)
        val transactionDateBefore = java.time.LocalDate.parse(endDate)
        return transactionRepository.findByTransactionDateBetween(transactionDateAfter, transactionDateBefore)
            .filter { userLocationUtils.locationPredicate(it) }
            .map { transactionMapper.toResponseDto(it) }
    }

    fun createTransaction(transactionInsertDto: TransactionInsertDto): TransactionResponseDto {
        if(contactCache.getAllContacts().find { it.id == transactionInsertDto.userId } == null){
            throw RuntimeException("User not found")
        }
        val newTransactionEntity = transactionMapper.toEntity(transactionInsertDto)
        populateLocation(newTransactionEntity)
        transactionCache.upsertTransaction(newTransactionEntity)
        
        val transactionDto = TransactionDto(
            newTransactionEntity.amount, 
            newTransactionEntity.transactionType, 
            newTransactionEntity.location,
            newTransactionEntity.transactionDate
        )
        
        contactNetStandingCache.adjust(newTransactionEntity.userId, null, transactionDto)
        dailyDebtSummaryCache.adjust(null, transactionDto)
        
        return transactionMapper.toResponseDto(newTransactionEntity)
    }

private fun populateLocation(entity: TransactionEntity) {
        if (LibertyPermissions.isLibertyAdmin()) {
            checkNotNull(entity.location) { "Location must be provided" }

        } else {
            userLocationUtils.populateLocation(entity)
        }
    }

    fun updateTransaction(updatedTransactionDto: TransactionUpdateDto): TransactionResponseDto {
        val existingTransaction = transactionCache.getTransactionById(updatedTransactionDto.id)
            ?: throw RuntimeException("Transaction not found")

        val oldTransaction = TransactionDto(
            existingTransaction.amount, 
            existingTransaction.transactionType, 
            existingTransaction.location,
            existingTransaction.transactionDate
        )
        val newTransaction = TransactionDto(
            updatedTransactionDto.amount?.orElse(existingTransaction.amount),
            updatedTransactionDto.transactionType?.orElse(existingTransaction.transactionType),
            existingTransaction.location,
            updatedTransactionDto.transactionDate?.orElse(existingTransaction.transactionDate)
        )
        contactNetStandingCache.adjust(existingTransaction.userId, oldTransaction, newTransaction)
        dailyDebtSummaryCache.adjust(oldTransaction, newTransaction)
        transactionMapper.partialUpdate(updatedTransactionDto, existingTransaction)
        transactionCache.upsertTransaction(existingTransaction)
        return transactionMapper.toResponseDto(existingTransaction)
    }
    
    fun deleteTransaction(id: UUID) {
        val txn = transactionCache.getTransactionById(id) ?: throw RuntimeException("Transaction not found")
        val transactionDto = TransactionDto(txn.amount, txn.transactionType, txn.location, txn.transactionDate)
        contactNetStandingCache.adjust(txn.userId, transactionDto, null)
        dailyDebtSummaryCache.adjust(transactionDto, null)
        transactionCache.deleteTransaction(id)
    }

}
