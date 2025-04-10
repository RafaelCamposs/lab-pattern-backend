package com.example.application.team.entrypoint.rest.dto.request

import com.example.domain.team.entity.dto.UpdateTeamDto

data class UpdateTeamRequestDto(
    val name: String,
    val description: String?,
    val imageUrl: String?,
) {
    fun toDomain(): UpdateTeamDto {
        return UpdateTeamDto(
            name = name,
            description = description,
            imageUrl = imageUrl,
            updatedAt = java.time.LocalDateTime.now()
        )
    }
}