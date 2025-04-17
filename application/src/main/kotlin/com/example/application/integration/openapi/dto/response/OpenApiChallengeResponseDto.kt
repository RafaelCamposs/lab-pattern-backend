package com.example.application.integration.openapi.dto.response

import com.example.domain.challenge.entity.dto.AiQuestionDto
import com.fasterxml.jackson.annotation.JsonProperty

data class OpenApiChallengeResponseDto(
    @JsonProperty(value = "description")
    val description: String,
    @JsonProperty(value = "title")
    val title: String,
) {
    fun toDomain(): AiQuestionDto {
        return AiQuestionDto(
            title = title,
            description = description,
        )
    }
}
