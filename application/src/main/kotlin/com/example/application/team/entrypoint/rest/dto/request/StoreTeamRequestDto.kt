package com.example.application.team.entrypoint.rest.dto.request

import com.example.domain.team.entity.dto.StoreTeamDto

data class StoreTeamRequestDto(
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val createdById: Long
) {
    fun toDomain(): StoreTeamDto {
        return StoreTeamDto(
            name = name,
            description = description,
            imageUrl = imageUrl,
            createdById = createdById
        )
    }
}