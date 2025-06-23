package me.ezrahome.libertyutils.debttracker.rest.user

import me.ezrahome.libertyutils.debttracker.business.user.dto.UserInsertDto
import me.ezrahome.libertyutils.debttracker.business.user.dto.UserUpdateDto
import me.ezrahome.libertyutils.debttracker.business.user.dto.UserResponseDto
import me.ezrahome.libertyutils.debttracker.business.user.UserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("secured/people")
class UserEndpoint(private val userService: UserService) {

    @PostMapping
    fun createUser(): SysUserDto = userService.addUser()
    
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
