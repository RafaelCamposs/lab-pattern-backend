package com.example.application.team.entrypoint.rest.dto.response

import com.example.domain.team.entity.Team
import java.time.LocalDateTime

data class TeamResponseDto(
    val id: Long?,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val createdById: Long,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun fromDomain(team: Team): TeamResponseDto {
            return TeamResponseDto(
                id = team.id,
                name = team.name,
                description = team.description,
                imageUrl = team.imageUrl,
                createdById = team.createdById,
                createdAt = team.createdAt,
                updatedAt = team.updatedAt
            )
        }
    }
}