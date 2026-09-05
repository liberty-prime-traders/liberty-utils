package me.ezrahome.libertyutils.debttracker.rest.contact

import me.ezrahome.libertyutils.debttracker.business.contact.ContactService
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactInsertDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactResponseDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactUpdateDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
@RequestMapping("secured/contacts")
class ContactsEndpoint(private val contactService: ContactService) {

    @PostMapping
    fun createUser(@RequestBody contactInsertDto: ContactInsertDto):
            ContactResponseDto = contactService.createContact(contactInsertDto)
    
    @PutMapping
    fun updateUser(@RequestBody contactUpdateDto: ContactUpdateDto): ContactResponseDto {
        return contactService.updateContact(contactUpdateDto)
    }

    @GetMapping
    fun getAllUsers(): Collection<ContactResponseDto> = contactService.getAllContacts()

    @PostMapping("refresh-balances")
    fun refreshBalances(): ResponseEntity<HttpStatus> {
        contactService.refreshBalances()
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun deleteUser(@PathVariable id: UUID?): ResponseEntity<HttpStatus> {
        contactService.deleteContact(id!!)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
