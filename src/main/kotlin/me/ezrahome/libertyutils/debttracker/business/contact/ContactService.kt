package me.ezrahome.libertyutils.debttracker.business.contact

import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactInsertDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactUpdateDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactResponseDto
import me.ezrahome.libertyutils.debttracker.business.contact.mapping.ContactMapper
import me.ezrahome.libertyutils.reusable.StringUtils
import org.springframework.stereotype.Service
import java.util.Objects
import java.util.UUID


@Service
class ContactService(
    private val contactMapper: ContactMapper,
    private val contactCache: ContactCache,
    private val contactBalanceCache: ContactBalanceCache
) {

    fun getAllContacts(): Collection<ContactResponseDto> {
        return contactCache.getAllContacts().map { entity ->
            val dto = contactMapper.toResponseDto(entity)
            dto.balance = contactBalanceCache.getBalance(entity.id)
            dto
        }
    }

    fun createContact(contactInsertDto: ContactInsertDto): ContactResponseDto {
        val value = StringUtils.getValueOrException(contactInsertDto.fullName, NAME_IS_REQUIRED)
        contactCache.getAllContacts().find { StringUtils.isEquivalent(it.fullName, value) }
            ?.let { throw RuntimeException(String.format(NAME_ALREADY_EXISTS, value)) }

        val newContactEntity = contactMapper.toEntity(contactInsertDto)
        contactCache.upsertContact(newContactEntity)
        return contactMapper.toResponseDto(newContactEntity)
    }

    fun updateContact(contactUpdateDto: ContactUpdateDto): ContactResponseDto {
        validateContactUpdate(contactUpdateDto)
        val contactToUpdate = contactCache.getAllContacts().find { Objects.equals(contactUpdateDto.id, it.id) }
            ?: throw RuntimeException("User not found")
        contactMapper.partialUpdate(contactUpdateDto, contactToUpdate)
        contactCache.upsertContact(contactToUpdate)
        return contactMapper.toResponseDto(contactToUpdate)
    }

    private fun validateContactUpdate(contactUpdateDto: ContactUpdateDto) {
        val value = StringUtils.getValueOrException(contactUpdateDto.fullName, NAME_IS_REQUIRED)
        contactCache.getAllContacts().find { it.fullName.equals(value, ignoreCase = true) && it.id != contactUpdateDto.id }
            ?.let{ throw RuntimeException(String.format(NAME_ALREADY_EXISTS, value)) }
    }

    fun deleteContact(id: UUID?) {
        contactCache.deleteContact(id!!)
    }

    companion object {
        const val NAME_IS_REQUIRED = "A contact must have a name"
        const val NAME_ALREADY_EXISTS = "A contact with the name %s already exists."
    }

}
