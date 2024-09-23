package com.devops7.user_service.services.userService

import com.devops7.user_service.exceptions.RoleNotFoundException
import com.devops7.user_service.models.User
import com.devops7.user_service.repository.UserRepository
import com.devops7.user_service.services.roleService.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val roleService: RoleService
) : UserService {

    override fun createUser(user: User): User {
        try {
            val role = roleService.getRoleById(user.roleId!!)
            return userRepository.save(user)
        } catch (ex: RoleNotFoundException) {
            throw IllegalArgumentException(ex.message)
        }
    }

    override fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    override fun updateUser(id: Long, updatedUser: User): User? {
        return userRepository.findById(id).map { existingUser ->
            val userToUpdate = existingUser.copy(
                name = updatedUser.name ?: existingUser.name,
                email = updatedUser.email ?: existingUser.email,
                password = updatedUser.password ?: existingUser.password,
                roleId = updatedUser.roleId ?: existingUser.roleId
            )
            userRepository.save(userToUpdate)
        }.orElse(null)
    }

    override fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}