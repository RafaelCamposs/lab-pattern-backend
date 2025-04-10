package com.example.domain.team.entity

import java.time.LocalDateTime

data class Team(
    val id: Long? = null,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val createdById: Long,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null
)