package com.example.application.challenge.model

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.entity.dto.StoreChallengeDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "challenge")
data class ChallengeModel (
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    val id: UUID? = null,

    val title : String,

    val description: String,

    val expectedPatternId: UUID,

    val publishedAt : LocalDateTime? = null,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val isDaily: Boolean = false,
) {
    fun toDomain() = Challenge(
        id = id!!,
        title = title,
        description = description,
        expectedPatternId = expectedPatternId,
        publishedAt = publishedAt!!,
        createdAt = createdAt
    )

    companion object {
        fun fromStoreChallengeDto(storeChallengeDto: StoreChallengeDto) =
            ChallengeModel(
                title = storeChallengeDto.title,
                description = storeChallengeDto.description,
                expectedPatternId = storeChallengeDto.expectedPatternId,
                publishedAt = storeChallengeDto.publishedAt,
                isDaily = storeChallengeDto.isDaily
            )
    }
}