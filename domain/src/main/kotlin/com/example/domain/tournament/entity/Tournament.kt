package com.example.domain.tournament.entity

import com.example.domain.tournament.entity.enum.TournamentStatusEnum
import java.time.LocalDateTime

data class Tournament(
    val id: Long? = null,
    val name: String,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val location: String,
    val description: String?,
    val logoUrl: String?,
    val status: TournamentStatusEnum,
    val category: String,
    val createdById: Long,
    val maxParticipants: Int,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null
) {
    fun isUpdatable() =
        status.isUpdatable()
        && deletedAt == null
        && LocalDateTime.now().isBefore(startDate)

    fun isDeletable() =
        status.isDeletable()
        && deletedAt == null
        && LocalDateTime.now().isBefore(startDate)
        && LocalDateTime.now().isAfter(endDate)
}
