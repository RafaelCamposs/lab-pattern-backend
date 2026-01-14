package com.example.submission

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetChallengeByIdGateway
import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.Feedback
import com.example.domain.evaluation.usecase.StoreEvaluationUseCase
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.gateway.GetPatternByIdGateway
import com.example.domain.submission.entity.Submission
import com.example.domain.user.entity.User
import com.example.domain.user.gateway.GetUserByIdGateway
import com.example.domain.submission.entity.dto.StoreSubmissionDto
import com.example.domain.submission.gateway.StoreSubmissionGateway
import com.example.domain.submission.usecase.StoreSubmissionUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class StoreSubmissionUseCaseTest {
    private val storeSubmissionGateway: StoreSubmissionGateway = mockk()
    private val getChallengeByIdGateway: GetChallengeByIdGateway = mockk()
    private val getUserByIdGateway: GetUserByIdGateway = mockk()
    private val getPatternByIdGateway: GetPatternByIdGateway = mockk()
    private val storeEvaluationGateway: com.example.domain.evaluation.gateway.StoreEvaluationGateway = mockk()
    private val evaluateSubmissionWithAiGateway: com.example.domain.evaluation.gateway.EvaluateSubmissionWithAiGateway = mockk()
    private val storeEvaluationUseCase: StoreEvaluationUseCase = StoreEvaluationUseCase(storeEvaluationGateway, evaluateSubmissionWithAiGateway)
    private val useCase = StoreSubmissionUseCase(
        storeSubmissionGateway,
        getChallengeByIdGateway,
        getUserByIdGateway,
        getPatternByIdGateway,
        storeEvaluationUseCase
    )

    @Test
    fun `should store submission successfully`() {
        val dto = StoreSubmissionDto(
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code",
            language = "kotlin",
            userId = UUID.randomUUID()
        )

        val challenge = Challenge(
            id = dto.challengeId,
            expectedPatternId = dto.patternId,
            title = "Challenge",
            description = "Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        val pattern = DesignPattern(
            id = dto.patternId,
            name = "Pattern",
            category = "Category",
            description = "Description"
        )

        val user = User(
            id = dto.userId,
            name = "Test User",
            email = "test@example.com",
            password = "password"
        )

        val submission = Submission(
            id = UUID.randomUUID(),
            challengeId = dto.challengeId,
            patternId = dto.patternId,
            code = dto.code,
            submittedAt = LocalDateTime.now(),
            language = dto.language,
            userId = dto.userId,
            evaluation = null
        )

        val evaluation = Evaluation(
            id = UUID.randomUUID(),
            submissionId = submission.id!!,
            score = 100,
            feedback = Feedback(
                content = listOf(),
                improvements = listOf(),
                strengths = listOf()
            ),
            evaluatedAt = LocalDateTime.now()
        )

        every { getChallengeByIdGateway.execute(dto.challengeId) } returns Result.success(challenge)
        every { getUserByIdGateway.execute(dto.userId) } returns Result.success(user)
        every { getPatternByIdGateway.execute(dto.patternId) } returns Result.success(pattern)
        every { storeSubmissionGateway.execute(dto) } returns Result.success(submission)
        every { evaluateSubmissionWithAiGateway.execute(submission = any(), challenge = any(), pattern = any()) } returns Result.success(
            com.example.domain.evaluation.entity.dto.AiEvaluationDto(
                score = 100,
                feedback = listOf(),
                strengths = listOf(),
                improvements = listOf()
            )
        )
        every { storeEvaluationGateway.execute(any()) } returns Result.success(evaluation)

        val result = useCase.execute(dto)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(evaluation, result.getOrNull()?.evaluation)
        verify { getChallengeByIdGateway.execute(dto.challengeId) }
        verify { getUserByIdGateway.execute(dto.userId) }
        verify { getPatternByIdGateway.execute(dto.patternId) }
        verify { storeSubmissionGateway.execute(dto) }
        verify { evaluateSubmissionWithAiGateway.execute(submission = any(), challenge = any(), pattern = any()) }
        verify { storeEvaluationGateway.execute(any()) }
    }

    @Test
    fun `should propagate challenge gateway error`() {
        val dto = StoreSubmissionDto(
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code",
            language = "kotlin",
            userId = UUID.randomUUID()
        )
        val error = RuntimeException("Challenge gateway error")
        
        every { getChallengeByIdGateway.execute(dto.challengeId) } returns Result.failure(error)

        val result = useCase.execute(dto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { getChallengeByIdGateway.execute(dto.challengeId) }
    }
}