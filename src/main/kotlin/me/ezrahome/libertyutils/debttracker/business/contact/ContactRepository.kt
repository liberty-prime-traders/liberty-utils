package me.ezrahome.libertyutils.debttracker.business.contact

import me.ezrahome.libertyutils.debttracker.model.ContactEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ContactRepository: JpaRepository<ContactEntity, UUID>