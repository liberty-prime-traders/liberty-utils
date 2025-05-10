package me.ezrahome.libertyutils.platform.model

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class AuditableEntity: BaseEntity()
