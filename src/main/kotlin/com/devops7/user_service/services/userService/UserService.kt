package com.devops7.user_service.services.userService

import com.devops7.user_service.models.User
import com.devops7.user_service.models.UserMappedToRole

interface UserService {
    fun createUser(user: User): UserMappedToRole
    fun getUserById(id: Long): UserMappedToRole?
    fun getAllUsers(): List<UserMappedToRole>
    fun updateUser(id: Long, updatedUser: User): UserMappedToRole?
    fun deleteUser(id: Long)
}