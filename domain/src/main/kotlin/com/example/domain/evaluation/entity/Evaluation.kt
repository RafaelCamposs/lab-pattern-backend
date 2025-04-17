package com.example.domain.evaluation.entity

import java.time.LocalDateTime
import java.util.UUID

data class Evaluation (
    val id: UUID? = null,
    val submissionId: UUID,
    val detectedPatternId: UUID,
    val score: Int,
    val feedback: String,
    val evaluatedAt: LocalDateTime
)