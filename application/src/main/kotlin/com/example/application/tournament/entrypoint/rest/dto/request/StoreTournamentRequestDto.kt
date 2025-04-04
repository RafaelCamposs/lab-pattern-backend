package com.example.application.tournament.entrypoint.rest.dto.request

import com.example.domain.tournament.entity.dto.StoreTournamentDto
import java.time.LocalDateTime

data class StoreTournamentRequestDto(
    val name: String,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val location: String,
    val maxParticipants: Int,
    val createdById: Long,
    val category: String
) {
    fun toDomain() = StoreTournamentDto(
        name = name,
        description = description,
        startDate = startDate,
        endDate = endDate,
        location = location,
        maxParticipants = maxParticipants,
        createdById = createdById,
        category = category
    )
}
