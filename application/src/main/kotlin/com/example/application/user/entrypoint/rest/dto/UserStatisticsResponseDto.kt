package com.example.application.user.entrypoint.rest.dto

import com.example.domain.user.entity.dto.UserStatisticsDto

data class UserStatisticsResponseDto(
    val completedChallenges: Int,
    val percentage: Int,
    val currentStreak: Int,
    val longestStreak: Int,
) {
    companion object {
        fun fromDomain(statistics: UserStatisticsDto): UserStatisticsResponseDto {
            return UserStatisticsResponseDto(
                completedChallenges = statistics.solvedChallenges,
                percentage = statistics.submissionCorrectnessPercentage,
                currentStreak = statistics.streaks.currentStreak,
                longestStreak = statistics.streaks.longestStreak
            )
        }
    }
}
