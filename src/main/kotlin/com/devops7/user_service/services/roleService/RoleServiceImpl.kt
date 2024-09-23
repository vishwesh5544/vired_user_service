package com.devops7.user_service.services.roleService

import com.devops7.user_service.exceptions.RoleNotFoundException
import com.devops7.user_service.models.Role
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException

@Service
class RoleServiceImpl(
    private val roleServiceWebClient: WebClient
) : RoleService {
    override fun getRoleById(roleId: String): Role {
        return try {
            roleServiceWebClient.get()
                .uri("/roles/$roleId")
                .retrieve()
                .bodyToMono(Role::class.java)
                .block() ?: throw RoleNotFoundException("Role with ID $roleId not found")
        } catch (ex: WebClientResponseException.NotFound) {
            throw RoleNotFoundException("Role with ID $roleId not found")
        } catch (ex: Exception) {
            throw RuntimeException("An error occurred while fetching the role: ${ex.message}")
        }
    }
}