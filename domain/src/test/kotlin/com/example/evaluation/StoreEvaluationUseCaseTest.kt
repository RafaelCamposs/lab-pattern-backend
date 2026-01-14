package com.example.evaluation

import com.example.domain.challenge.entity.Challenge
import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.dto.AiEvaluationDto
import com.example.domain.evaluation.gateway.EvaluateSubmissionWithAiGateway
import com.example.domain.evaluation.gateway.StoreEvaluationGateway
import com.example.domain.evaluation.usecase.StoreEvaluationUseCase
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.submission.entity.Submission
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class StoreEvaluationUseCaseTest {
    private val storeEvaluationGateway: StoreEvaluationGateway = mockk()
    private val evaluateSubmissionWithAiGateway: EvaluateSubmissionWithAiGateway = mockk()

    private val useCase = StoreEvaluationUseCase(
        storeEvaluationGateway,
        evaluateSubmissionWithAiGateway
    )

    @Test
    fun `should store evaluation successfully`() {
        val submission = Submission(
            id = UUID.randomUUID(),
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = UUID.randomUUID(),
            evaluation = null
        )

        val challenge = Challenge(
            id = submission.challengeId,
            expectedPatternId = submission.patternId,
            title = "Challenge",
            description = "Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        val pattern = DesignPattern(
            id = submission.patternId,
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )

        val aiEvaluation = AiEvaluationDto(
            score = 85,
            feedback = listOf("Good implementation"),
            strengths = listOf("Clean code structure"),
            improvements = listOf("Could improve error handling")
        )

        val evaluation = Evaluation(
            id = UUID.randomUUID(),
            submissionId = submission.id!!,
            score = aiEvaluation.score,
            feedback = aiEvaluation.getFeedback(),
            evaluatedAt = LocalDateTime.now()
        )

        every {
            evaluateSubmissionWithAiGateway.execute(pattern, challenge, submission)
        } returns Result.success(aiEvaluation)

        every {
            storeEvaluationGateway.execute(match {
                it.submissionId == submission.id &&
                it.userId == submission.userId &&
                it.aiEvaluationDto == aiEvaluation
            })
        } returns Result.success(evaluation)

        val result = useCase.execute(submission, challenge, pattern)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(evaluation, result.getOrNull())
        verify {
            evaluateSubmissionWithAiGateway.execute(pattern, challenge, submission)
            storeEvaluationGateway.execute(any())
        }
    }

    @Test
    fun `should propagate ai evaluation gateway error`() {
        val submission = Submission(
            id = UUID.randomUUID(),
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = UUID.randomUUID(),
            evaluation = null
        )

        val challenge = Challenge(
            id = submission.challengeId,
            expectedPatternId = submission.patternId,
            title = "Challenge",
            description = "Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        val pattern = DesignPattern(
            id = submission.patternId,
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )

        val error = RuntimeException("AI evaluation error")

        every {
            evaluateSubmissionWithAiGateway.execute(pattern, challenge, submission)
        } returns Result.failure(error)

        val result = useCase.execute(submission, challenge, pattern)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify {
            evaluateSubmissionWithAiGateway.execute(pattern, challenge, submission)
        }
    }

    @Test
    fun `should propagate store evaluation gateway error`() {
        val submission = Submission(
            id = UUID.randomUUID(),
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = UUID.randomUUID(),
            evaluation = null
        )

        val challenge = Challenge(
            id = submission.challengeId,
            expectedPatternId = submission.patternId,
            title = "Challenge",
            description = "Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        val pattern = DesignPattern(
            id = submission.patternId,
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )

        val aiEvaluation = AiEvaluationDto(
            score = 85,
            feedback = listOf("Good implementation"),
            strengths = listOf("Clean code structure"),
            improvements = listOf("Could improve error handling")
        )

        val error = RuntimeException("Storage error")

        every {
            evaluateSubmissionWithAiGateway.execute(pattern, challenge, submission)
        } returns Result.success(aiEvaluation)

        every {
            storeEvaluationGateway.execute(any())
        } returns Result.failure(error)

        val result = useCase.execute(submission, challenge, pattern)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify {
            evaluateSubmissionWithAiGateway.execute(pattern, challenge, submission)
            storeEvaluationGateway.execute(any())
        }
    }
}
