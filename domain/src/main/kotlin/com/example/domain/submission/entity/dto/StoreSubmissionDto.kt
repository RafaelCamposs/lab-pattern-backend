package com.example.domain.submission.entity.dto

import java.util.*

data class StoreSubmissionDto(
    val userId: UUID,
    val challengeId: UUID,
    val code: String,
    val language: String,
)
