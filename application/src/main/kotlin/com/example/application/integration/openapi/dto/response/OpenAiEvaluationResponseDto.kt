package com.example.application.integration.openapi.dto.response

import com.example.domain.evaluation.entity.dto.AiEvaluationDto
import com.fasterxml.jackson.annotation.JsonProperty

data class OpenAiEvaluationResponseDto(
    @field:JsonProperty(value = "score")
    val score: Int,
    @field:JsonProperty(value = "feedback")
    val feedback: List<String>,
    @field:JsonProperty(value = "strengths")
    val strengths: List<String>,
    @field:JsonProperty(value = "improvements")
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
