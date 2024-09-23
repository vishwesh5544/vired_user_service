package com.devops7.user_service.services.roleService

import com.devops7.user_service.models.Role

interface RoleService {
    fun getRoleById(roleId: String): Role
}