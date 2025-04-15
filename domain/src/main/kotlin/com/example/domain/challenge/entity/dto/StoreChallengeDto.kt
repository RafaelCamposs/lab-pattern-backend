package com.example.domain.challenge.entity.dto

import java.time.LocalDateTime
import java.util.UUID

data class StoreChallengeDto (
    val title: String,
    val expectedPatternId: UUID,
    val description: String,
    val publishedAt: LocalDateTime = LocalDateTime.now()
)
