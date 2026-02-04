package com.example.submission

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetChallengeByIdGateway
import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.Feedback
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.gateway.GetPatternByIdGateway
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetSubmissionByIdGateway
import com.example.domain.submission.usecase.GetSubmissionByIdUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetSubmissionByIdUseCaseTest {
    private val getSubmissionByIdGateway: GetSubmissionByIdGateway = mockk()
    private val getChallengeByIdGateway: GetChallengeByIdGateway = mockk()
    private val getPatternByIdGateway: GetPatternByIdGateway = mockk()
    private val useCase = GetSubmissionByIdUseCase(
        getSubmissionByIdGateway,
        getChallengeByIdGateway,
        getPatternByIdGateway
    )

    @Test
    fun `should get submission by id successfully`() {
        val id = UUID.randomUUID()
        val challengeId = UUID.randomUUID()
        val selectedPatternId = UUID.randomUUID()
        val expectedPatternId = UUID.randomUUID()

        val submission = Submission(
            id = id,
            challengeId = challengeId,
            patternId = selectedPatternId,
            code = "code",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = UUID.randomUUID(),
            evaluation = Evaluation(
                id = UUID.randomUUID(),
                submissionId = id,
                score = 100,
                feedback = Feedback(
                    content = listOf(),
                    improvements = listOf(),
                    strengths = listOf()
                ),
                evaluatedAt = LocalDateTime.now()
            )
        )

        val challenge = Challenge(
            id = challengeId,
            expectedPatternId = expectedPatternId,
            title = "Challenge",
            description = "Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        val selectedPattern = DesignPattern(
            id = selectedPatternId,
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )

        val expectedPattern = DesignPattern(
            id = expectedPatternId,
            name = "Observer",
            category = "Behavioral",
            description = "Observer pattern description"
        )

        every { getSubmissionByIdGateway.execute(id) } returns Result.success(submission)
        every { getChallengeByIdGateway.execute(challengeId) } returns Result.success(challenge)
        every { getPatternByIdGateway.execute(selectedPatternId) } returns Result.success(selectedPattern)
        every { getPatternByIdGateway.execute(expectedPatternId) } returns Result.success(expectedPattern)

        val result = useCase.execute(id)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals("Strategy", result.getOrNull()?.selectedPatternName)
        Assertions.assertEquals("Observer", result.getOrNull()?.expectedPatternName)
        verify { getSubmissionByIdGateway.execute(id) }
        verify { getChallengeByIdGateway.execute(challengeId) }
        verify { getPatternByIdGateway.execute(selectedPatternId) }
        verify { getPatternByIdGateway.execute(expectedPatternId) }
    }

    @Test
    fun `should propagate gateway error`() {
        val id = UUID.randomUUID()
        val error = RuntimeException("Gateway error")
        every { getSubmissionByIdGateway.execute(id) } returns Result.failure(error)

        val result = useCase.execute(id)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { getSubmissionByIdGateway.execute(id) }
    }
}