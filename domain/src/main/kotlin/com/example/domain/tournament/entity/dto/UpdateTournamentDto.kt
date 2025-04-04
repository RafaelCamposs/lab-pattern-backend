package com.example.domain.tournament.entity.dto

import java.time.LocalDateTime

data class UpdateTournamentDto(
    val name: String,
    val description: String,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val location: String,
    val maxParticipants: Int
)
