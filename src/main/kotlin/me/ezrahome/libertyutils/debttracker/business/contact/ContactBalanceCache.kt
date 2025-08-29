package me.ezrahome.libertyutils.debttracker.business.contact

import jakarta.annotation.PostConstruct
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class ContactBalanceCache(
    private val transactionRepository: TransactionRepository
) {
    private val balances: MutableMap<UUID, BigDecimal> = ConcurrentHashMap()

    @PostConstruct
    fun init() {
        val all = transactionRepository.findAllUserBalances()
        all.forEach { projection ->
            balances[projection.userId] = projection.balance ?: BigDecimal.ZERO
        }
    }

    fun getBalance(userId: UUID?): BigDecimal {
        if (userId == null) return BigDecimal.ZERO
        return balances[userId] ?: BigDecimal.ZERO
    }

    fun adjust(userId: UUID?, delta: BigDecimal?) {
        if (userId == null || delta == null) return
        balances.compute(userId) { _, existing ->
            val current = existing ?: BigDecimal.ZERO
            current.add(delta)
        }
    }
}
