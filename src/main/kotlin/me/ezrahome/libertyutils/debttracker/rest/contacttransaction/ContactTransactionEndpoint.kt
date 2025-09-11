package me.ezrahome.libertyutils.debttracker.rest.contacttransaction

import jakarta.websocket.server.PathParam
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionService
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("secured/contact_transaction")
class ContactTransactionEndpoint(private val transactionService: TransactionService) {

    @GetMapping(params = ["userId"])
    fun getLast5Transactions(
        @PathParam("userId") userId: UUID,
    ): Collection<TransactionResponseDto> {
        return transactionService.getContactLast5Transactions(userId)
    }
}
