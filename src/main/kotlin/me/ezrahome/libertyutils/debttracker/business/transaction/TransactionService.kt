package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import me.ezrahome.libertyutils.debttracker.business.contact.ContactCache
import org.springframework.stereotype.Service
import java.util.Objects
import java.util.UUID

@Service
class TransactionService(
    private val transactionMapper: TransactionMapper,
    private val transactionCache: TransactionCache,
    private val contactCache: ContactCache
) {

    fun getAllTransactions(): Collection<TransactionResponseDto> {
        return transactionCache.getAllTransactions().map { transactionMapper.toResponseDto(it) }
    }

    fun createTransaction(transactionInsertDto: TransactionInsertDto): TransactionResponseDto {
        if(contactCache.getAllContacts().find { it.id == transactionInsertDto.userID } == null){
            throw RuntimeException("User not found")
        }
        val newTransactionEntity = transactionMapper.toEntity(transactionInsertDto)
        transactionCache.upsertTransaction(newTransactionEntity)
        return transactionMapper.toResponseDto(newTransactionEntity)
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