package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.configuration.cache.CacheNames
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.reusable.model.LibertyLocation
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
    fun getTransactionById(id: UUID): TransactionEntity? =
        transactionRepository.findById(id).orElse(null)

    @Cacheable
    fun getLatestTransactions(locations: Set<LibertyLocation>): List<TransactionEntity> =
        transactionRepository.findByLocationInOrderByTransactionDateDesc(locations, PageRequest.of(0, 5))

    @CacheEvict(allEntries = true)
    fun upsertTransaction(transactionEntity: TransactionEntity): TransactionEntity =
        transactionRepository.save(transactionEntity)

    @CacheEvict(allEntries = true)
    fun deleteTransaction(id: UUID) {
        transactionRepository.deleteById(id)
    }
}
