package me.ezrahome.libertyutils.debttracker.business.transaction.mapping

import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.debttracker.model.TransactionType
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.Optional

@Component
object TransactionMappingQualifier {

    @SignedAmount
    fun getSignedAmount(transactionEntity: TransactionEntity): String? {
        val signedAmount = when (transactionEntity.transactionType) {
            TransactionType.CREDIT -> transactionEntity.amount
            TransactionType.DEBIT -> transactionEntity.amount?.negate()
            else -> BigDecimal.ZERO
        }
        return signedAmount?.toPlainString()
    }

    @AbsoluteValue
    fun getAbsoluteValue(amount: BigDecimal?): String? {
        return amount?.abs()?.toPlainString()
    }

    @AbsoluteValue
    fun getAbsoluteValue(amount: Optional<BigDecimal>?): String? {
        return amount?.orElse(null)?.abs()?.toPlainString()
    }
}
