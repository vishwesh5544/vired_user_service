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

    private val userCache = Caffeine.newBuilder()
        .expireAfterWrite(4, TimeUnit.HOURS)
        .maximumSize(100)
        .build<Long, User>()  // Cache user by ID

    override fun createUser(user: User): UserMappedToRole {
        return try {
            val role = roleService.getRoleById(user.roleId!!)
            val createdUser = userRepository.save(user)
            userCache.put(createdUser.id!!, createdUser)

            mapToUserMappedToRole(createdUser, role)
        } catch (ex: RoleNotFoundException) {
            throw IllegalArgumentException(ex.message)
        }
    }

    override fun getUserById(id: Long): UserMappedToRole? {
        val user = userCache.get(id) {
            userRepository.findById(id).orElse(null)
        }

        if (user == null) return null

        val role = roleService.getRoleById(user.roleId!!)
        return mapToUserMappedToRole(user, role)
    }

    override fun getAllUsers(): List<UserMappedToRole> {
        val users = userRepository.findAll()

        return users.mapNotNull { user ->
            try {
                val role = roleService.getRoleById(user.roleId!!)
                mapToUserMappedToRole(user, role)
            } catch (ex: RoleNotFoundException) {
                null
            }
        }
    }

    override fun updateUser(id: Long, updatedUser: User): UserMappedToRole? {
        return userRepository.findById(id).map { existingUser ->
            val userToUpdate = existingUser.copy(
                name = updatedUser.name ?: existingUser.name,
                email = updatedUser.email ?: existingUser.email,
                password = updatedUser.password ?: existingUser.password,
                roleId = updatedUser.roleId ?: existingUser.roleId
            )

            val savedUser = userRepository.save(userToUpdate)
            userCache.put(savedUser.id!!, savedUser)

            val role = roleService.getRoleById(savedUser.roleId!!)
            mapToUserMappedToRole(savedUser, role)
        }.orElse(null)
    }

    override fun deleteUser(id: Long) {
        userRepository.deleteById(id)
        userCache.invalidate(id)
    }

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
