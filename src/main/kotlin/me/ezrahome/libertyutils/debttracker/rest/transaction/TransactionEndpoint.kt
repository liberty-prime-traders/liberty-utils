package me.ezrahome.libertyutils.debttracker.rest.transaction

import jakarta.websocket.server.PathParam
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionService
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("secured/transaction")
class TransactionEndpoint(private val transactionService: TransactionService) {

    @PostMapping
    fun createTransaction(@RequestBody transactionInsertDto: TransactionInsertDto):
            TransactionResponseDto = transactionService.createTransaction(transactionInsertDto)

    @PutMapping
    fun updateTransaction(@RequestBody transactionUpdateDto: TransactionUpdateDto): TransactionResponseDto {
        return transactionService.updateTransaction(transactionUpdateDto)
    }

    @PostMapping("fetch-by-date")
    fun getTransactionsBetweenDates(@RequestBody dates: Collection<LocalDate>):  Map<String, Collection<TransactionResponseDto>> {
        return transactionService.getTransactionsForTransactionDates(dates).asMap()
    }

    @GetMapping(params = ["userId"])
    fun getLast5Transactions(@PathParam("userId") userId: UUID): Map<UUID, Collection<TransactionResponseDto>> {
        return transactionService.getContactLast5Transactions(userId).asMap()
    }

    @DeleteMapping("{id}")
    fun deleteTransaction(@PathVariable id: UUID): TransactionResponseDto {
        return transactionService.deleteTransaction(id)
    }
}
