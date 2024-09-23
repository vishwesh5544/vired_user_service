package com.devops7.user_service.services.userService

import com.devops7.user_service.models.User

interface UserService {
    fun createUser(user: User): User
    fun getUserById(id: Long): User?
    fun getAllUsers(): List<User>
    fun updateUser(id: Long, updatedUser: User): User?
    fun deleteUser(id: Long)
}