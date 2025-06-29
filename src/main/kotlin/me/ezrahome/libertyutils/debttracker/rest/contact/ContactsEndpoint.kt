package me.ezrahome.libertyutils.debttracker.rest.contact

import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactInsertDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactUpdateDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactResponseDto
import me.ezrahome.libertyutils.debttracker.business.contact.ContactService
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
    
    @DeleteMapping("{id}")
    fun deleteUser(@PathVariable("id") id: UUID?) {
        contactService.deleteUser(id!!)
    }
}
