package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.configuration.security.LibertyPermissions
import me.ezrahome.libertyutils.debttracker.business.contact.ContactNetStandingCache
import me.ezrahome.libertyutils.debttracker.business.contact.ContactCache
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import me.ezrahome.libertyutils.debttracker.business.transaction.mapping.TransactionMapper
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.platform.business.audit.AuditFetcher
import me.ezrahome.libertyutils.platform.business.audit.MasterAuditDto
import me.ezrahome.libertyutils.platform.business.audit.MasterAuditor
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import me.ezrahome.libertyutils.reusable.classes.BatchExecutor
import me.ezrahome.libertyutils.reusable.constants.TableNames
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Transactional
@Service
class TransactionService(
    private val transactionMapper: TransactionMapper,
    private val transactionCache: TransactionCache,
    private val contactCache: ContactCache,
    private val auditFetcher: AuditFetcher,
    private val userLocationUtils: UserLocationUtils,
    private val contactNetStandingCache: ContactNetStandingCache,
    private val transactionRepository: TransactionRepository,
    private val masterAuditor: MasterAuditor,
) {

    @Transactional(readOnly = true)
    fun getTransactionsForTransactionDates(dates: Collection<LocalDate>): Map<String, List<TransactionResponseDto>> {
        val result = mutableMapOf<String, MutableList<TransactionResponseDto>>()
        BatchExecutor.findAllBySomeKeyIn(dates, transactionRepository::findTransactionsByTransactionDateIn)
            .filter { userLocationUtils.locationPredicate(it) }
            .forEach {
                val transactionDto = transactionMapper.toResponseDto(it)
                result.getOrPut(transactionDto.transactionDate!!) { mutableListOf() }.add(transactionDto)
            }
        return result
    }

    fun createTransaction(transactionInsertDto: TransactionInsertDto): TransactionResponseDto {
        if(contactCache.getAllContacts().find { it.id == transactionInsertDto.userId } == null){
            throw RuntimeException("User not found")
        }
        val newTransactionEntity = transactionMapper.toEntity(transactionInsertDto)
        populateLocation(newTransactionEntity)
        transactionCache.upsertTransaction(newTransactionEntity)
        masterAuditor.logInsert(newTransactionEntity)
        contactNetStandingCache.adjust(
            newTransactionEntity.userId,
            null,
            TransactionDto(newTransactionEntity.amount, newTransactionEntity.transactionType, newTransactionEntity.location)
        )
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

        val oldTransaction = TransactionDto(existingTransaction.amount, existingTransaction.transactionType, existingTransaction.location)
        val newTransaction = TransactionDto(
            updatedTransactionDto.amount?.orElse(existingTransaction.amount),
            updatedTransactionDto.transactionType?.orElse(existingTransaction.transactionType),
            existingTransaction.location
        )
        val oldEntityState = transactionMapper.cloneEntity(existingTransaction)
        contactNetStandingCache.adjust(existingTransaction.userId, oldTransaction, newTransaction)
        transactionMapper.partialUpdate(updatedTransactionDto, existingTransaction)
        transactionCache.upsertTransaction(existingTransaction)
        masterAuditor.logUpdate(oldEntityState, existingTransaction)
        return transactionMapper.toResponseDto(existingTransaction)
    }

    fun deleteTransaction(id: UUID): TransactionResponseDto {
        val txn = transactionCache.getTransactionById(id) ?: throw RuntimeException("Transaction not found")
        contactNetStandingCache.adjust(txn.userId, TransactionDto(txn.amount, txn.transactionType, txn.location), null)
        transactionCache.deleteTransaction(id)
        masterAuditor.logDelete(txn)
        return transactionMapper.toResponseDto(txn)
    }

    fun getContactLast5Transactions(userId: UUID): Map<UUID, List<TransactionResponseDto>> {
        val transactions = transactionCache.getLast5Transactions(userId)
            .map { transactionMapper.toResponseDto(it) }
        return mapOf(userId to transactions)
    }

    @Transactional(readOnly = true)
    fun getAuditRecords(recordId: String): Collection<MasterAuditDto> {
        val recordGuid = UUID.fromString(recordId)
        return auditFetcher.getAuditRecords(TableNames.TRANSACTION, recordGuid, TransactionEntity::class.java)
    }
}
