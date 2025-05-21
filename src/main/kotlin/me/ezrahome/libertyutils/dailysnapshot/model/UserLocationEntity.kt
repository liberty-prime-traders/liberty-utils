package me.ezrahome.libertyutils.dailysnapshot.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import me.ezrahome.libertyutils.platform.constants.TableNames
import me.ezrahome.libertyutils.platform.model.ExpirableAssignmentEntity

@Entity
@Table(name = TableNames.USER_LOCATION)
class UserLocationEntity(
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "location_name", nullable = false)
    var location: LibertyLocation? = null
) : ExpirableAssignmentEntity()
