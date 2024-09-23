package com.devops7.user_service.models

import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Data
@Table(name = "users")  // Maps to "users" table in PostgreSQL
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String? = null,

    @Column(nullable = false, unique = true)
    var email: String? = null,

    @Column(nullable = false)
    var password: String? = null,

    @Column(nullable = false)
    var roleId: String? = null,  // Role ID associated with the user

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
)
