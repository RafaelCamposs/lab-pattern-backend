package com.example.domain.team.entity.dto

data class UpdateTeamDto(
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val updatedAt: java.time.LocalDateTime
)
