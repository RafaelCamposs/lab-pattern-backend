package com.example.domain.evaluation.entity.dto

data class AiEvaluationDto(
    val score: Int,
    val feedback: String,
    val strengths: List<String>,
    val improvements: List<String>,
)
