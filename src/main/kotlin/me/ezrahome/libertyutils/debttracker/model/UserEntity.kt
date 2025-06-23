package me.ezrahome.libertyutils.debttracker.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import me.ezrahome.libertyutils.platform.constants.TableNames
import me.ezrahome.libertyutils.platform.model.AuditableEntity

@Entity
@Table(name = TableNames.USER)
class UserEntity(
    @NotNull
    @Column(name = "full_name", nullable = false)
    var FullName: String? = null,

    @Column(name = "relationship")
    var Relationship: String? = null,

    @Column(name = "email")
    var Email: String? = null,

    @Column(name = "phone_number")
    var PhoneNumber: String? = null

) : AuditableEntity()