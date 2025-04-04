package com.example.application.tournament.entrypoint.rest.dto.response

import com.example.domain.tournament.entity.Tournament

data class TournamentResponseDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val startDate: String,
    val endDate: String,
    val location: String,
    val maxParticipants: Int,
    val createdById: Long,
    val createdAt: String,
    val updatedAt: String,
) {
    companion object {
        fun fromDomain(tournament: Tournament) = TournamentResponseDto(
            id = tournament.id!!,
            name = tournament.name,
            description = tournament.description,
            startDate = tournament.startDate.toString(),
            endDate = tournament.endDate.toString(),
            location = tournament.location,
            maxParticipants = tournament.maxParticipants,
            createdById = tournament.createdById,
            createdAt = tournament.createdAt.toString(),
            updatedAt = tournament.updatedAt.toString()
        )
    }
}
