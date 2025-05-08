package com.example.application.challenge.entrypoint.rest.dto.response

import com.example.domain.challenge.entity.Challenge
import java.time.LocalDateTime
import java.util.*

data class ChallengeResponseDto(
    val id: UUID,
    val description: String,
    val title: String,
    val isDaily: Boolean,
    val publishedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(challenge: Challenge): ChallengeResponseDto {
            return ChallengeResponseDto(
                id = challenge.id,
                description = challenge.description,
                title = challenge.title,
                isDaily = challenge.isDaily,
                publishedAt = challenge.publishedAt
            )
        }

        fun fromPageDomain(
            page: com.example.domain.common.Page<Challenge>
        ): com.example.domain.common.Page<ChallengeResponseDto> {
            return com.example.domain.common.Page(
                content = page.content.map { fromDomain(it) },
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                currentPage = page.currentPage,
                pageSize = page.pageSize,
                isLast = page.isLast
            )
        }
    }
}
