package me.ezrahome.libertyutils.debttracker.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import me.ezrahome.libertyutils.reusable.constants.TableNames
import me.ezrahome.libertyutils.reusable.model.AuditableEntity

@Entity
@Table(name = TableNames.CONTACT)
class ContactEntity(
    @NotNull
    @Column(name = "full_name", nullable = false)
    var fullName: String? = null,

    @Column(name = "contact_type")
    var contactType: ContactType? = null,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "phone_number")
    var phoneNumber: String? = null

) : AuditableEntity()
