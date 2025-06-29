package me.ezrahome.libertyutils.debttracker.rest.transaction

import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.TransactionService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@CrossOrigin
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

    @DeleteMapping("{id}")
    fun deleteTransaction(@PathVariable("id") id: UUID?) {
        transactionService.deleteTransaction(id!!)
    }
}
