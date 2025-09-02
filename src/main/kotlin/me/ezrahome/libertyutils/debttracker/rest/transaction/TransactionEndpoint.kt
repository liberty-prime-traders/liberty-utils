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

    @GetMapping
    fun getAllTransactions(): Collection<TransactionResponseDto> = transactionService.getAllTransactions()

    @GetMapping(params = ["startDate", "endDate"])
    fun getTransactionsBetweenDates(
        @PathParam("startDate") startDate: String,
        @PathParam("endDate") endDate: String
    ): Collection<TransactionResponseDto> {

        return transactionService.getTransactionsForTransactionDate(startDate, endDate)
    }

    @DeleteMapping("{id}")
    fun deleteTransaction(@PathVariable("id") id: UUID?) {
        transactionService.deleteTransaction(id!!)
    }
}
