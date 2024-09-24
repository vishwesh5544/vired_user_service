package com.devops7.user_service.services.userService

import com.devops7.user_service.exceptions.RoleNotFoundException
import com.devops7.user_service.models.User
import com.devops7.user_service.repository.UserRepository
import com.devops7.user_service.services.roleService.RoleService
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val roleService: RoleService
) : UserService {

    // Caffeine cache for users (expire entries after 10 minutes)
    private val userCache = Caffeine.newBuilder()
        .expireAfterWrite(4, TimeUnit.HOURS)
        .maximumSize(100)
        .build<Long, User>()  // Cache user by ID

    override fun createUser(user: User): User {
        try {
            val role = roleService.getRoleById(user.roleId!!)
            val createdUser = userRepository.save(user)
            userCache.put(createdUser.id!!, createdUser)  // Cache the created user
            return createdUser
        } catch (ex: RoleNotFoundException) {
            throw IllegalArgumentException(ex.message)
        }
    }

    override fun getUserById(id: Long): User? {
        return userCache.get(id) {
            userRepository.findById(id).orElse(null)
        }
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
            val savedUser = userRepository.save(userToUpdate)
            userCache.put(savedUser.id!!, savedUser)
            savedUser
        }.orElse(null)
    }

    override fun deleteUser(id: Long) {
        userRepository.deleteById(id)
        userCache.invalidate(id)
    }
}
