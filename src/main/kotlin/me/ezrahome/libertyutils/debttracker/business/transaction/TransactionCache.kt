package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.platform.configuration.cache.CacheNames
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@CacheConfig(cacheNames = [CacheNames.TRANSACTION])
class TransactionCache(
    private val transactionRepository: TransactionRepository,
) {

    @Cacheable
    fun getAllTransactions(): Collection<TransactionEntity> = transactionRepository.findAll()

    @CacheEvict(allEntries = true)
    fun upsertTransaction(transactionEntity: TransactionEntity): TransactionEntity = transactionRepository.save(transactionEntity)

    @CacheEvict(allEntries = true)
    fun deleteTransaction(id: UUID) {
        transactionRepository.deleteById(id)
    }
}
