package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.dailysnapshot.business.user_location.UserLocationRepository
import me.ezrahome.libertyutils.debttracker.business.contact.ContactBalanceCache
import me.ezrahome.libertyutils.debttracker.business.contact.ContactCache
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionDto
import me.ezrahome.libertyutils.dailysnapshot.model.UserLocationEntity
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import me.ezrahome.libertyutils.platform.configuration.security.LibertyPermissions
import me.ezrahome.libertyutils.platform.configuration.session.SessionContextProvider
import org.springframework.stereotype.Service
import java.math.BigDecimal
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.Objects
import java.util.UUID

@Service
class TransactionService(
    private val transactionMapper: TransactionMapper,
    private val transactionCache: TransactionCache,
    private val contactCache: ContactCache,
    private val userLocationRepository: UserLocationRepository,
    private val contactBalanceCache: ContactBalanceCache
    private val transactionRepository: TransactionRepository,
) {

    fun getAllTransactions(): Collection<TransactionResponseDto> {
        return transactionCache.getAllTransactions().map { transactionMapper.toResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun getTransactionsForTransactionDate(startDate: String, endDate: String): Collection<TransactionResponseDto> {
        val transactionDateAfter = LocalDate.parse(startDate)
        val transactionDateBefore = LocalDate.parse(endDate)
        val currentLocation = getUserLocation()
        val predicate: (TransactionEntity) -> Boolean = { tx ->
            LibertyPermissions.isLibertyAdmin() || tx.location == currentLocation.location
        }

        val transactions = transactionRepository.findTransactionsByTransactionDateBetween(transactionDateAfter, transactionDateBefore)

        return transactions
            .filter(predicate)
            .map { transactionMapper.toResponseDto(it) }
    }

    private fun getUserLocation(): UserLocationEntity =
        userLocationRepository.findByUserIdAndEndOnNull(SessionContextProvider.getUserId())
            ?: throw RuntimeException("User location not found")

    fun createTransaction(transactionInsertDto: TransactionInsertDto): TransactionResponseDto {
        if(contactCache.getAllContacts().find { it.id == transactionInsertDto.userId } == null){
            throw RuntimeException("User not found")
        }
        val newTransactionEntity = transactionMapper.toEntity(transactionInsertDto)
        populateLocation(newTransactionEntity)
        transactionCache.upsertTransaction(newTransactionEntity)
        val delta = getChangeInOverallBalance(null,TransactionDto(newTransactionEntity.amount, newTransactionEntity.transactionType))
        contactBalanceCache.adjust(newTransactionEntity.userId, delta)
        return transactionMapper.toResponseDto(newTransactionEntity)
    }

private fun populateLocation(entity: TransactionEntity) {
        if (LibertyPermissions.isLibertyAdmin()) {
            checkNotNull(entity.location) { "Location must be provided" }

        } else {
            val userLocation = userLocationRepository.findByUserIdAndEndOnNull(SessionContextProvider.getUserId())
                ?: throw RuntimeException("User location not found")
            entity.location = userLocation.location
        }
    }

    fun updateTransaction(updatedTransactionDto: TransactionUpdateDto): TransactionResponseDto {
        val existingTransaction = transactionCache.getAllTransactions().find { Objects.equals(updatedTransactionDto.id, it.id) }
            ?: throw RuntimeException("Transaction not found")

        val oldTransaction = TransactionDto(existingTransaction.amount, existingTransaction.transactionType)
        val newTransaction = TransactionDto(
            updatedTransactionDto.amount?.orElse(existingTransaction.amount),
            updatedTransactionDto.transactionType?.orElse(existingTransaction.transactionType)
        )
        val changeInBalance = getChangeInOverallBalance(oldTransaction, newTransaction)
        contactBalanceCache.adjust(existingTransaction.userId, changeInBalance)
        transactionMapper.partialUpdate(updatedTransactionDto, existingTransaction)
        transactionCache.upsertTransaction(existingTransaction)
        return transactionMapper.toResponseDto(existingTransaction)
    }
    
    fun deleteTransaction(id: UUID?) {
        val txn = transactionCache.getAllTransactions().find { it.id == id }
            ?: throw RuntimeException("Transaction not found")
        val delta = getChangeInOverallBalance(TransactionDto(txn.amount, txn.transactionType), null)
        contactBalanceCache.adjust(txn.userId, delta)
        transactionCache.deleteTransaction(id!!)
    }

    private fun getChangeInOverallBalance(oldTransaction: TransactionDto?, newTransaction: TransactionDto?): BigDecimal {
        val oldAmount = oldTransaction?.amount ?: BigDecimal.ZERO
        val newAmount = newTransaction?.amount ?: BigDecimal.ZERO

        val oldChange = when (oldTransaction?.transactionType) {
            TransactionType.DEBIT -> oldAmount
            TransactionType.CREDIT -> oldAmount.negate()
            else -> BigDecimal.ZERO
        }

        val newChange = when (newTransaction?.transactionType) {
            TransactionType.DEBIT -> newAmount
            TransactionType.CREDIT -> newAmount.negate()
            else -> BigDecimal.ZERO
        }

        return newChange.minus(oldChange)
    }
}
