package me.ezrahome.libertyutils.debttracker.business.contact

import me.ezrahome.libertyutils.platform.configuration.cache.CacheNames
import me.ezrahome.libertyutils.debttracker.model.ContactEntity
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@CacheConfig(cacheNames = [CacheNames.CONTACT])
class ContactCache(
    private val contactRepository: ContactRepository,
) {

    @Cacheable
    fun getAllContacts(): Collection<ContactEntity> = contactRepository.findAll()

    @CacheEvict(allEntries = true)
    fun upsertContact(contactEntity: ContactEntity):
            ContactEntity = contactRepository.save(contactEntity)

    @CacheEvict(allEntries = true)
    fun deleteContact(id: UUID) {
        contactRepository.deleteById(id)
    }
}
