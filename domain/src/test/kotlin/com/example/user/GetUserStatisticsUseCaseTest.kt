package com.example.user

import com.example.domain.challenge.gateway.CountUserSolvedChallengesGateway
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetAllUserSubmissionsWithEvaluationsGateway
import com.example.domain.submission.usecase.GetSubmissionCorrectnessPercentageUseCase
import com.example.domain.user.entity.dto.StreaksDto
import com.example.domain.user.usecase.GetUserStatisticsUseCase
import com.example.domain.user.usecase.GetUserStreaksUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetUserStatisticsUseCaseTest {
    private val countUserSolvedChallengesGateway: CountUserSolvedChallengesGateway = mockk()
    private val getAllUserSubmissionsGateway: GetAllUserSubmissionsWithEvaluationsGateway = mockk()
    private val getUserStreaksUseCase: GetUserStreaksUseCase = GetUserStreaksUseCase()
    private val getSubmissionCorrectnessPercentageUseCase: GetSubmissionCorrectnessPercentageUseCase = GetSubmissionCorrectnessPercentageUseCase()

    private val useCase = GetUserStatisticsUseCase(
        countUserSolvedChallengesGateway,
        getAllUserSubmissionsGateway,
        getUserStreaksUseCase,
        getSubmissionCorrectnessPercentageUseCase
    )

    @Test
    fun `should get user statistics successfully`() {
        val userId = UUID.randomUUID()
        val solvedChallenges = 5

        val evaluation = com.example.domain.evaluation.entity.Evaluation(
            id = UUID.randomUUID(),
            submissionId = UUID.randomUUID(),
            score = 85,
            feedback = com.example.domain.evaluation.entity.Feedback(
                content = listOf("Good implementation"),
                strengths = listOf("Clean code"),
                improvements = listOf("Better error handling")
            ),
            evaluatedAt = LocalDateTime.now()
        )

        val submissions = listOf(
            Submission(
                id = UUID.randomUUID(),
                challengeId = UUID.randomUUID(),
                patternId = UUID.randomUUID(),
                code = "code",
                submittedAt = LocalDateTime.now(),
                language = "kotlin",
                userId = userId,
                evaluation = evaluation
            )
        )

        val correctnessPercentage = 85
        val streaks = StreaksDto(currentStreak = 1, longestStreak = 1)

        every { countUserSolvedChallengesGateway.execute(userId) } returns Result.success(solvedChallenges)
        every { getAllUserSubmissionsGateway.execute(userId) } returns Result.success(submissions)

        val result = useCase.execute(userId)

        Assertions.assertTrue(result.isSuccess)
        val statistics = result.getOrNull()
        Assertions.assertNotNull(statistics)
        Assertions.assertEquals(solvedChallenges, statistics?.solvedChallenges)
        Assertions.assertEquals(correctnessPercentage, statistics?.submissionCorrectnessPercentage)
        Assertions.assertEquals(streaks, statistics?.streaks)

        verify { countUserSolvedChallengesGateway.execute(userId) }
        verify { getAllUserSubmissionsGateway.execute(userId) }
    }

    @Test
    fun `should propagate count solved challenges gateway error`() {
        val userId = UUID.randomUUID()
        val error = RuntimeException("Count challenges error")

        every { countUserSolvedChallengesGateway.execute(userId) } returns Result.failure(error)

        Assertions.assertThrows(RuntimeException::class.java) {
            useCase.execute(userId)
        }

        verify { countUserSolvedChallengesGateway.execute(userId) }
    }

    @Test
    fun `should propagate get all submissions gateway error`() {
        val userId = UUID.randomUUID()
        val solvedChallenges = 5
        val error = RuntimeException("Get submissions error")

        every { countUserSolvedChallengesGateway.execute(userId) } returns Result.success(solvedChallenges)
        every { getAllUserSubmissionsGateway.execute(userId) } returns Result.failure(error)

        Assertions.assertThrows(RuntimeException::class.java) {
            useCase.execute(userId)
        }

        verify { countUserSolvedChallengesGateway.execute(userId) }
        verify { getAllUserSubmissionsGateway.execute(userId) }
    }

}
