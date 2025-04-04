package com.example.application.tournament.model

import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.entity.dto.StoreTournamentDto
import com.example.domain.tournament.entity.enum.TournamentStatusEnum
import jakarta.persistence.*
import org.hibernate.annotations.Where
import java.time.LocalDateTime

@Entity(name = "tournament")
@Where(clause = "deleted_at IS NULL")
data class TournamentModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    val description: String? = null,

    val startDate: LocalDateTime? = null,

    val endDate: LocalDateTime? = null,

    val location: String,

    val category: String,

    val logoUrl: String? = null,

    val maxParticipants: Int,

    @Column(name = "status", columnDefinition = "varchar(50)")
    val status: TournamentStatusEnum,

    val deletedAt: LocalDateTime? = null,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime = LocalDateTime.now(),

    val createdById: Long,
) {
    companion object {
        fun fromStoreTournamentDto(
            storeTournamentDto: StoreTournamentDto
        )= TournamentModel(
            name = storeTournamentDto.name,
            description = storeTournamentDto.description,
            startDate = storeTournamentDto.startDate,
            endDate = storeTournamentDto.endDate,
            status = TournamentStatusEnum.CREATED,
            location = storeTournamentDto.location,
            maxParticipants = storeTournamentDto.maxParticipants,
            createdById = storeTournamentDto.createdById,
            category = storeTournamentDto.category,
        )

        fun fromDomain(tournament: Tournament) = TournamentModel(
            id = tournament.id,
            name = tournament.name,
            description = tournament.description,
            category = tournament.category,
            startDate = tournament.startDate,
            endDate = tournament.endDate,
            status = tournament.status,
            location = tournament.location,
            maxParticipants = tournament.maxParticipants,
            createdById = tournament.createdById
        )
    }

    fun toDomain() = Tournament(
        id = id,
        name = name,
        description = description,
        startDate = startDate,
        endDate = endDate,
        maxParticipants = maxParticipants,
        location = location,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
        createdById = createdById,
        logoUrl = logoUrl,
        category = category
    )
}
