package com.example.domain.challenge.entity

import java.time.LocalDateTime
import java.util.*

data class Challenge(
    val id: UUID,
    val expectedPatternId: UUID,
    val title: String,
    val description: String,
    val publishedAt: LocalDateTime,
    val createdAt: LocalDateTime,
)
