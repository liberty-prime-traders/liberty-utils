package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.configuration.cache.CacheNames
import me.ezrahome.libertyutils.debttracker.business.summary.dto.LatestTransactionsDto
import me.ezrahome.libertyutils.debttracker.business.summary.dto.TopContactDto
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@CacheConfig(cacheNames = [CacheNames.TRANSACTION])
class TransactionCache(
    private val transactionRepository: TransactionRepository,
) {

    @Cacheable
    fun getAllTransactions(): Collection<TransactionEntity> = transactionRepository.findAll()

    @Cacheable
    fun getLatestTransactions(): List<LatestTransactionsDto> = transactionRepository.findLatestTransactions(PageRequest.of(0, 5))

    @Cacheable
    fun findTopUsersByType(type: TransactionType): List<TopContactDto> = transactionRepository.findTopUsersByType(type,PageRequest.of(0, 5))

    @CacheEvict(allEntries = true)
    fun upsertTransaction(transactionEntity: TransactionEntity): TransactionEntity = transactionRepository.save(transactionEntity)

    @CacheEvict(allEntries = true)
    fun deleteTransaction(id: UUID) {
        transactionRepository.deleteById(id)
    }
}
