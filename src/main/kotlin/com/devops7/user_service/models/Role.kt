package com.devops7.user_service.models

data class Role(
    val id: String,
    val name: String,
    val permissions: List<String>? = null,
    val createdAt: String,
    val updatedAt: String
)