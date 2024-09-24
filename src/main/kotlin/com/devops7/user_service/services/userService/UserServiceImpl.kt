package com.devops7.user_service.services.userService

import com.devops7.user_service.exceptions.RoleNotFoundException
import com.devops7.user_service.models.Role
import com.devops7.user_service.models.User
import com.devops7.user_service.models.UserMappedToRole
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

    // Caffeine cache for users (expire entries after 4 hours)
    private val userCache = Caffeine.newBuilder()
        .expireAfterWrite(4, TimeUnit.HOURS)
        .maximumSize(100)
        .build<Long, User>()  // Cache user by ID

    override fun createUser(user: User): UserMappedToRole {
        return try {
            // Fetch role and create user
            val role = roleService.getRoleById(user.roleId!!)
            val createdUser = userRepository.save(user)
            userCache.put(createdUser.id!!, createdUser)  // Cache the created user

            // Return mapped user with role
            mapToUserMappedToRole(createdUser, role)
        } catch (ex: RoleNotFoundException) {
            throw IllegalArgumentException(ex.message)
        }
    }

    override fun getUserById(id: Long): UserMappedToRole? {
        // Try to get user from cache first
        val user = userCache.get(id) {
            userRepository.findById(id).orElse(null)
        }

        // Return null if user not found
        if (user == null) return null

        // Fetch role from RoleService
        val role = roleService.getRoleById(user.roleId!!)
        return mapToUserMappedToRole(user, role)
    }

    override fun getAllUsers(): List<UserMappedToRole> {
        // Get all users from the repository
        val users = userRepository.findAll()

        // For each user, fetch the role and map to UserMappedToRole
        return users.mapNotNull { user ->
            try {
                val role = roleService.getRoleById(user.roleId!!)
                mapToUserMappedToRole(user, role)
            } catch (ex: RoleNotFoundException) {
                null // Skip users where the role is not found
            }
        }
    }

    override fun updateUser(id: Long, updatedUser: User): UserMappedToRole? {
        return userRepository.findById(id).map { existingUser ->
            // Update the user fields
            val userToUpdate = existingUser.copy(
                name = updatedUser.name ?: existingUser.name,
                email = updatedUser.email ?: existingUser.email,
                password = updatedUser.password ?: existingUser.password,
                roleId = updatedUser.roleId ?: existingUser.roleId
            )

            // Save the updated user and cache it
            val savedUser = userRepository.save(userToUpdate)
            userCache.put(savedUser.id!!, savedUser)

            // Fetch role and return the mapped object
            val role = roleService.getRoleById(savedUser.roleId!!)
            mapToUserMappedToRole(savedUser, role)
        }.orElse(null)
    }

    override fun deleteUser(id: Long) {
        userRepository.deleteById(id)
        userCache.invalidate(id)  // Invalidate the cache for the deleted user
    }

    // Helper function to map User entity to UserMappedToRole
    private fun mapToUserMappedToRole(user: User, role: Role): UserMappedToRole {
        return UserMappedToRole(
            id = user.id,
            name = user.name,
            email = user.email,
            password = user.password,
            roleId = role  // Map the role
        )
    }
}
