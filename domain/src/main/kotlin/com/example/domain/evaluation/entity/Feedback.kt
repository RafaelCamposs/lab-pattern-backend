package com.example.domain.evaluation.entity

data class Feedback(
    val content: List<String>,
    val strengths: List<String>,
    val improvements: List<String>,
)
