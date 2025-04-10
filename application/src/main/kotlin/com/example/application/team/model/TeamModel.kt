package com.example.application.team.model

import com.example.domain.team.entity.Team
import com.example.domain.team.entity.dto.StoreTeamDto
import jakarta.persistence.*
import org.hibernate.annotations.Where
import java.time.LocalDateTime

@Entity
@Table(name = "team")
@Where(clause = "deleted_at IS NULL")
data class TeamModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val name: String,
    
    val description: String?,

    val imageUrl: String?,

    val createdById: Long,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime = LocalDateTime.now(),

    val deletedAt: LocalDateTime? = null
) {
    companion object {
        fun fromStoreTeamDto(storeTeamDto: StoreTeamDto) = TeamModel(
            name = storeTeamDto.name,
            description = storeTeamDto.description,
            imageUrl = storeTeamDto.imageUrl,
            createdById = storeTeamDto.createdById,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    fun toDomain() = Team(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        createdById = createdById,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}