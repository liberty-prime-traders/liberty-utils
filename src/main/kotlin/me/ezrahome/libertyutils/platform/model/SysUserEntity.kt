package me.ezrahome.libertyutils.platform.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import me.ezrahome.libertyutils.reusable.constants.TableNames
import me.ezrahome.libertyutils.reusable.model.BaseEntity

@Entity
@Table(name = TableNames.SYS_USER)
class SysUserEntity(
    @Size(max = 50)
    @NotNull
    @Column(name = "okta_id", nullable = false, length = 50)
    var oktaId: String? = null
) : BaseEntity()
