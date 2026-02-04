package com.example.user

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetChallengeByIdGateway
import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.Feedback
import com.example.domain.evaluation.gateway.GetEvaluationsBySubmissionIdsGateway
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.gateway.GetPatternByIdGateway
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetSubmissionByIdGateway
import com.example.domain.submission.gateway.GetSubmissionsByChallengeAndUserIdGateway
import com.example.domain.submission.usecase.GetSubmissionByIdUseCase
import com.example.domain.user.usecase.GetUserSubmissionsByChallengeIdUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetUserSubmissionsByChallengeIdUseCaseTest {
    private val getSubmissionsByChallengeAndUserIdGateway: GetSubmissionsByChallengeAndUserIdGateway = mockk()
    private val getEvaluationsBySubmissionIdsGateway: GetEvaluationsBySubmissionIdsGateway = mockk()
    private val getSubmissionByIdGateway: GetSubmissionByIdGateway = mockk()
    private val getChallengeByIdGateway: GetChallengeByIdGateway = mockk()
    private val getPatternByIdGateway: GetPatternByIdGateway = mockk()
    private val getSubmissionByIdUseCase = GetSubmissionByIdUseCase(
        getSubmissionByIdGateway,
        getChallengeByIdGateway,
        getPatternByIdGateway
    )

    private val useCase = GetUserSubmissionsByChallengeIdUseCase(
        getSubmissionsByChallengeAndUserIdGateway,
        getEvaluationsBySubmissionIdsGateway,
        getSubmissionByIdUseCase
    )

    @Test
    fun `should get user submissions with evaluations successfully`() {
        val challengeId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val expectedPatternId = UUID.randomUUID()

        val submission1 = Submission(
            id = UUID.randomUUID(),
            challengeId = challengeId,
            patternId = UUID.randomUUID(),
            code = "code1",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = userId,
            evaluation = null
        )

        val submission2 = Submission(
            id = UUID.randomUUID(),
            challengeId = challengeId,
            patternId = UUID.randomUUID(),
            code = "code2",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = userId,
            evaluation = null
        )

        val submissions = listOf(submission1, submission2)

        val challenge = Challenge(
            id = challengeId,
            expectedPatternId = expectedPatternId,
            title = "Challenge",
            description = "Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        val selectedPattern1 = DesignPattern(id = submission1.patternId, name = "Strategy", category = "Behavioral", description = "Strategy description")
        val selectedPattern2 = DesignPattern(id = submission2.patternId, name = "Strategy", category = "Behavioral", description = "Strategy description")
        val expectedPattern = DesignPattern(id = expectedPatternId, name = "Observer", category = "Behavioral", description = "Observer description")

        val evaluation1 = Evaluation(
            id = UUID.randomUUID(),
            submissionId = submission1.id!!,
            score = 85,
            feedback = Feedback(
                content = listOf("Good"),
                improvements = listOf("Could improve"),
                strengths = listOf("Clean code")
            ),
            evaluatedAt = LocalDateTime.now()
        )

        val evaluation2 = Evaluation(
            id = UUID.randomUUID(),
            submissionId = submission2.id!!,
            score = 90,
            feedback = Feedback(
                content = listOf("Excellent"),
                improvements = listOf("Minor issues"),
                strengths = listOf("Great structure")
            ),
            evaluatedAt = LocalDateTime.now()
        )

        val evaluations = listOf(evaluation1, evaluation2)

        every { getSubmissionsByChallengeAndUserIdGateway.execute(challengeId, userId) } returns Result.success(submissions)
        every { getSubmissionByIdGateway.execute(submission1.id!!) } returns Result.success(submission1)
        every { getSubmissionByIdGateway.execute(submission2.id!!) } returns Result.success(submission2)
        every { getChallengeByIdGateway.execute(challengeId) } returns Result.success(challenge)
        every { getPatternByIdGateway.execute(submission1.patternId) } returns Result.success(selectedPattern1)
        every { getPatternByIdGateway.execute(submission2.patternId) } returns Result.success(selectedPattern2)
        every { getPatternByIdGateway.execute(expectedPatternId) } returns Result.success(expectedPattern)
        every { getEvaluationsBySubmissionIdsGateway.execute(listOf(submission1.id, submission2.id)) } returns Result.success(evaluations)

        val result = useCase.execute(challengeId, userId)

        assertTrue(result.isSuccess)
        val resultSubmissions = result.getOrNull()
        assertNotNull(resultSubmissions)
        assertEquals(2, resultSubmissions.size)
        assertEquals(evaluation1, resultSubmissions[0].evaluation)
        assertEquals(evaluation2, resultSubmissions[1].evaluation)
        assertEquals("Strategy", resultSubmissions[0].selectedPatternName)
        assertEquals("Observer", resultSubmissions[0].expectedPatternName)

        verify { getSubmissionsByChallengeAndUserIdGateway.execute(challengeId, userId) }
        verify { getSubmissionByIdGateway.execute(submission1.id!!) }
        verify { getSubmissionByIdGateway.execute(submission2.id!!) }
        verify { getEvaluationsBySubmissionIdsGateway.execute(listOf(submission1.id, submission2.id)) }
    }

    @Test
    fun `should handle submissions without evaluations`() {
        val challengeId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val expectedPatternId = UUID.randomUUID()

        val submission = Submission(
            id = UUID.randomUUID(),
            challengeId = challengeId,
            patternId = UUID.randomUUID(),
            code = "code",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = userId,
            evaluation = null
        )

        val submissions = listOf(submission)

        val challenge = Challenge(
            id = challengeId,
            expectedPatternId = expectedPatternId,
            title = "Challenge",
            description = "Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        val selectedPattern = DesignPattern(id = submission.patternId, name = "Strategy", category = "Behavioral", description = "Strategy description")
        val expectedPattern = DesignPattern(id = expectedPatternId, name = "Observer", category = "Behavioral", description = "Observer description")

        every { getSubmissionsByChallengeAndUserIdGateway.execute(challengeId, userId) } returns Result.success(submissions)
        every { getSubmissionByIdGateway.execute(submission.id!!) } returns Result.success(submission)
        every { getChallengeByIdGateway.execute(challengeId) } returns Result.success(challenge)
        every { getPatternByIdGateway.execute(submission.patternId) } returns Result.success(selectedPattern)
        every { getPatternByIdGateway.execute(expectedPatternId) } returns Result.success(expectedPattern)
        every { getEvaluationsBySubmissionIdsGateway.execute(listOf(submission.id!!)) } returns Result.success(emptyList())

        val result = useCase.execute(challengeId, userId)

        assertTrue(result.isSuccess)
        val resultSubmissions = result.getOrNull()
        assertNotNull(resultSubmissions)
        assertEquals(1, resultSubmissions.size)
        assertNull(resultSubmissions[0].evaluation)

        verify { getSubmissionsByChallengeAndUserIdGateway.execute(challengeId, userId) }
        verify { getSubmissionByIdGateway.execute(submission.id!!) }
        verify { getEvaluationsBySubmissionIdsGateway.execute(listOf(submission.id!!)) }
    }

    @Test
    fun `should propagate get submissions gateway error`() {
        val challengeId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val error = RuntimeException("Get submissions error")

        every {
            getSubmissionsByChallengeAndUserIdGateway.execute(challengeId, userId)
        } returns Result.failure(error)

        assertThrows(RuntimeException::class.java) {
            useCase.execute(challengeId, userId)
        }

        verify { getSubmissionsByChallengeAndUserIdGateway.execute(challengeId, userId) }
    }

    @Test
    fun `should propagate get evaluations gateway error`() {
        val challengeId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val expectedPatternId = UUID.randomUUID()

        val submission = Submission(
            id = UUID.randomUUID(),
            challengeId = challengeId,
            patternId = UUID.randomUUID(),
            code = "code",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = userId,
            evaluation = null
        )

        val submissions = listOf(submission)
        val error = RuntimeException("Get evaluations error")

        val challenge = Challenge(
            id = challengeId,
            expectedPatternId = expectedPatternId,
            title = "Challenge",
            description = "Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        val selectedPattern = DesignPattern(id = submission.patternId, name = "Strategy", category = "Behavioral", description = "Strategy description")
        val expectedPattern = DesignPattern(id = expectedPatternId, name = "Observer", category = "Behavioral", description = "Observer description")

        every { getSubmissionsByChallengeAndUserIdGateway.execute(challengeId, userId) } returns Result.success(submissions)
        every { getSubmissionByIdGateway.execute(submission.id!!) } returns Result.success(submission)
        every { getChallengeByIdGateway.execute(challengeId) } returns Result.success(challenge)
        every { getPatternByIdGateway.execute(submission.patternId) } returns Result.success(selectedPattern)
        every { getPatternByIdGateway.execute(expectedPatternId) } returns Result.success(expectedPattern)
        every { getEvaluationsBySubmissionIdsGateway.execute(listOf(submission.id!!)) } returns Result.failure(error)

        assertThrows(RuntimeException::class.java) {
            useCase.execute(challengeId, userId)
        }

        verify { getSubmissionsByChallengeAndUserIdGateway.execute(challengeId, userId) }
        verify { getSubmissionByIdGateway.execute(submission.id!!) }
        verify { getEvaluationsBySubmissionIdsGateway.execute(listOf(submission.id!!)) }
    }
}
