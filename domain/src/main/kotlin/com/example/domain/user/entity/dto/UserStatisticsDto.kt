package com.example.domain.user.entity.dto

data class UserStatisticsDto(
    val solvedChallenges: Int,
    val submissionCorrectnessPercentage: Int,
    val streaks: StreaksDto
)