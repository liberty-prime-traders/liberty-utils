package me.ezrahome.libertyutils.debttracker.business.user

import me.ezrahome.libertyutils.debttracker.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<UserEntity, UUID>