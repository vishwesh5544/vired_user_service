package com.devops7.user_service.controller

import com.devops7.user_service.models.User
import com.devops7.user_service.services.userService.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    @Autowired private val userService: UserService
) {

    @PostMapping("/")
    fun createUser(@RequestBody user: User): ResponseEntity<Any> {
        return try {
            val createdUser = userService.createUser(user)
            ResponseEntity(createdUser, HttpStatus.CREATED)
        } catch (ex: IllegalArgumentException) {
            ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
        } catch (ex: Exception) {
            ResponseEntity("An unexpected error occurred: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<Any> {
        val user = userService.getUserById(id)
        return if (user != null) {
            ResponseEntity(user, HttpStatus.OK)
        } else {
            ResponseEntity("User not found", HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/")
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userService.getAllUsers()
        return ResponseEntity(users, HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updatedUser: User): ResponseEntity<Any> {
        return try {
            val user = userService.updateUser(id, updatedUser)
            if (user != null) {
                ResponseEntity(user, HttpStatus.OK)
            } else {
                ResponseEntity("User not found", HttpStatus.NOT_FOUND)
            }
        } catch (ex: IllegalArgumentException) {
            ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            userService.deleteUser(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (ex: Exception) {
            ResponseEntity("An unexpected error occurred: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}