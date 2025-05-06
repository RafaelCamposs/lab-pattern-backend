package com.example.domain.user.usecase

import com.example.domain.challenge.gateway.CountUserSolvedChallengesGateway
import com.example.domain.submission.gateway.GetAllUserSubmissionsWithEvaluationsGateway
import com.example.domain.submission.usecase.GetSubmissionCorrectnessPercentageUseCase
import com.example.domain.user.entity.dto.UserStatisticsDto
import jakarta.inject.Named
import java.util.UUID

@Named
class GetUserStatisticsUseCase (
    private val countUserSolvedChallengesGateway: CountUserSolvedChallengesGateway,
    private val getAllUserSubmissionsGateway: GetAllUserSubmissionsWithEvaluationsGateway,
    private val getUserStreaksUseCase: GetUserStreaksUseCase,
    private val getSubmissionCorrectnessPercentageUseCase: GetSubmissionCorrectnessPercentageUseCase,
) {
    fun execute(userId: UUID) : Result<UserStatisticsDto> {
        val solvedChallenges = countUserSolvedChallengesGateway.execute(userId).getOrThrow()
        val submissions = getAllUserSubmissionsGateway.execute(userId).getOrThrow()
        val correctnessPercentage = getSubmissionCorrectnessPercentageUseCase.execute(submissions).getOrThrow()
        val streaks = getUserStreaksUseCase.execute(submissions).getOrThrow()

        return Result.success(
            UserStatisticsDto(
                solvedChallenges = solvedChallenges,
                submissionCorrectnessPercentage = correctnessPercentage,
                streaks = streaks
            )
        )
    }
}