package com.example.application.evaluation.entrypoint.rest.dto.response

import com.example.domain.evaluation.entity.Evaluation

data class EvaluationResponseDto(
    val score: Int,
    val feedback: FeedbackResponseDto
) {
    companion object {
        fun fromDomain(evaluation : Evaluation) = EvaluationResponseDto(
            score = evaluation.score,
            feedback = FeedbackResponseDto.fromDomain(evaluation.feedback)
        )
    }
}
