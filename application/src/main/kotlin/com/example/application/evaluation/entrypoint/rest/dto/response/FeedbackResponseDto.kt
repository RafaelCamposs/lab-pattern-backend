package com.example.application.evaluation.entrypoint.rest.dto.response

import com.example.domain.evaluation.entity.Feedback

data class FeedbackResponseDto(
    val keyPoints: List<String>,
    val strengths: List<String>,
    val improvements: List<String>
) {
    companion object {
        fun fromDomain(feedback: Feedback) = FeedbackResponseDto(
            keyPoints = feedback.content,
            strengths = feedback.strengths,
            improvements = feedback.improvements
        )
    }
}
