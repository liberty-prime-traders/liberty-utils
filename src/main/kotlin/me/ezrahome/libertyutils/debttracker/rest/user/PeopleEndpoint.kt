package me.ezrahome.libertyutils.debttracker.rest.user

import me.ezrahome.libertyutils.debttracker.business.user.dto.UserInsertDto
import me.ezrahome.libertyutils.debttracker.business.user.dto.UserUpdateDto
import me.ezrahome.libertyutils.debttracker.business.user.dto.UserResponseDto
import me.ezrahome.libertyutils.debttracker.business.user.UserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@CrossOrigin
@RestController
@RequestMapping("secured/people")
class PeopleEndpoint(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody userInsertDto: UserInsertDto): UserResponseDto = userService.createUser(userInsertDto)
    
    @PutMapping
    fun updateUser(@RequestBody userUpdateDto: UserUpdateDto): UserResponseDto {
        return userService.updateUser(userUpdateDto)
    }

    @GetMapping
    fun getAllUsers(): Collection<UserResponseDto> = userService.getAllUsers()
    
    @DeleteMapping("{id}")
    fun deleteUser(@PathVariable("id") id: UUID?) {
        userService.deleteUser(id!!)
    }
}
