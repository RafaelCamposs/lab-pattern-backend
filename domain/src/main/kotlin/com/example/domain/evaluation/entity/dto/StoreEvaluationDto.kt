package com.example.domain.evaluation.entity.dto

import java.util.UUID

data class StoreEvaluationDto (
    val submissionId: UUID,
    val userId: UUID,
    val aiEvaluationDto: AiEvaluationDto,
)