package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.dailysnapshot.business.user_location.UserLocationRepository
import me.ezrahome.libertyutils.dailysnapshot.model.UserLocationEntity
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import me.ezrahome.libertyutils.debttracker.business.contact.ContactCache
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.platform.configuration.security.LibertyPermissions
import me.ezrahome.libertyutils.platform.configuration.session.SessionContextProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.Objects
import java.util.UUID

@Service
class TransactionService(
    private val transactionMapper: TransactionMapper,
    private val transactionCache: TransactionCache,
    private val contactCache: ContactCache,
    private val transactionRepository: TransactionRepository,
    private val userLocationRepository: UserLocationRepository
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

    fun updateTransaction(transactionDto: TransactionUpdateDto): TransactionResponseDto {
        val transactionToUpdate = transactionCache.getAllTransactions().find { Objects.equals(transactionDto.id, it.id) }
            ?: throw RuntimeException("Transaction not found")
        transactionMapper.partialUpdate(transactionDto, transactionToUpdate)
        transactionCache.upsertTransaction(transactionToUpdate)
        return transactionMapper.toResponseDto(transactionToUpdate)
    }
    
    fun deleteTransaction(id: UUID?) {
        transactionCache.deleteTransaction(id!!)
    }
}