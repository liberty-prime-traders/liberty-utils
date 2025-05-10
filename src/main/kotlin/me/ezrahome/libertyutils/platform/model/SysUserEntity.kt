package me.ezrahome.libertyutils.platform.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import me.ezrahome.libertyutils.platform.constants.TableNames

@Entity
@Table(name = TableNames.SYS_USER)
class SysUserEntity(
    @Size(max = 50)
    @NotNull
    @Column(name = "okta_id", nullable = false, length = 50)
    var oktaId: String? = null
) : BaseEntity()
