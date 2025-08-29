package me.ezrahome.libertyutils.reusable.model

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class AuditableEntity: BaseEntity()
