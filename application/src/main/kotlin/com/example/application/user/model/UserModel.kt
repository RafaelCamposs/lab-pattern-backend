package com.example.application.user.model

import com.example.domain.user.entity.User
import jakarta.persistence.*
import org.hibernate.annotations.Where
import java.time.LocalDateTime

@Entity(name = "user")
@Where(clause = "deleted_at is null")
data class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    @Column(unique = true)
    val email: String,

    val password: String,

    val deletedAt: LocalDateTime? = null,
) {
    fun toDomain() = User(
        id = id,
        name = name,
        email = email,
        password = password
    )
}
