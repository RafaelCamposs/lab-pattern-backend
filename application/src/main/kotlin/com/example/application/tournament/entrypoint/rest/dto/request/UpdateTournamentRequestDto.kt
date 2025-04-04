package com.example.application.tournament.entrypoint.rest.dto.request

import com.example.domain.tournament.entity.dto.UpdateTournamentDto

data class UpdateTournamentRequestDto(
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val maxParticipants: Int,
) {
    fun toDomain() = UpdateTournamentDto(
        name = name,
        description = description,
        startDate = java.time.LocalDateTime.parse(startDate),
        endDate = java.time.LocalDateTime.parse(endDate),
        location = location,
        maxParticipants = maxParticipants
    )
}
