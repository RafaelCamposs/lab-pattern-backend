package com.example.application.integration.openapi.dto.response

import com.example.domain.evaluation.entity.dto.AiEvaluationDto
import com.fasterxml.jackson.annotation.JsonProperty

data class OpenAiEvaluationResponseDto(
    @JsonProperty(value = "score")
    val score: Int,
    @JsonProperty(value = "feedback")
    val feedback: String,
    @JsonProperty(value = "strengths")
    val strengths: List<String>,
    @JsonProperty(value = "improvements")
    val improvements: List<String>,
) {
    fun toDomain(): AiEvaluationDto {
        return AiEvaluationDto(
            score = score,
            feedback = feedback,
            strengths = strengths,
            improvements = improvements
        )
    }
}
