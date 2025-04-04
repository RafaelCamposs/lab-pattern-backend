package com.example.domain.tournament.entity.dto

import java.time.LocalDateTime

data class StoreTournamentDto(
    val name: String,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val location: String,
    val maxParticipants: Int,
    val category: String,
    val createdById: Long
)
