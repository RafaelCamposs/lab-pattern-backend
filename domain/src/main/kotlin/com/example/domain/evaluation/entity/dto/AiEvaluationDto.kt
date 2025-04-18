package com.example.domain.evaluation.entity.dto

import com.example.domain.evaluation.entity.Feedback

data class AiEvaluationDto(
    val score: Int,
    val feedback: List<String>,
    val strengths: List<String>,
    val improvements: List<String>,
) {
    fun getFeedback() = Feedback(
        content = feedback,
        strengths = strengths,
        improvements = improvements
    )
}
