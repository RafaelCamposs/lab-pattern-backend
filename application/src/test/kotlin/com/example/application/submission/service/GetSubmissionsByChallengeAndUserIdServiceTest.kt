package com.example.application.submission.service

import com.example.application.submission.model.SubmissionModel
import com.example.application.submission.repository.SubmissionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetSubmissionsByChallengeAndUserIdServiceTest {
    private val submissionRepository: SubmissionRepository = mockk()

    private val service = GetSubmissionsByChallengeAndUserIdService(submissionRepository)

    @Test
    fun `should get submissions by challenge and user id successfully`() {
        val challengeId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val submissionModels = listOf(
            SubmissionModel(
                id = UUID.randomUUID(),
                userId = userId,
                challengeId = challengeId,
                patternId = UUID.randomUUID(),
                code = "code1",
                language = "kotlin",
                submittedAt = LocalDateTime.now()
            ),
            SubmissionModel(
                id = UUID.randomUUID(),
                userId = userId,
                challengeId = challengeId,
                patternId = UUID.randomUUID(),
                code = "code2",
                language = "kotlin",
                submittedAt = LocalDateTime.now()
            )
        )

        every {
            submissionRepository.findByUserIdAndChallengeId(userId, challengeId)
        } returns submissionModels

        val result = service.execute(challengeId, userId)

        Assertions.assertTrue(result.isSuccess)
        val submissions = result.getOrNull()
        Assertions.assertNotNull(submissions)
        Assertions.assertEquals(2, submissions?.size)
        Assertions.assertEquals(submissionModels[0].id, submissions?.get(0)?.id)
        Assertions.assertEquals(submissionModels[1].id, submissions?.get(1)?.id)
        verify { submissionRepository.findByUserIdAndChallengeId(userId, challengeId) }
    }

    @Test
    fun `should return empty list when no submissions found`() {
        val challengeId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        every {
            submissionRepository.findByUserIdAndChallengeId(userId, challengeId)
        } returns emptyList()

        val result = service.execute(challengeId, userId)

        Assertions.assertTrue(result.isSuccess)
        val submissions = result.getOrNull()
        Assertions.assertNotNull(submissions)
        Assertions.assertEquals(0, submissions?.size)
        verify { submissionRepository.findByUserIdAndChallengeId(userId, challengeId) }
    }

    @Test
    fun `should propagate repository error`() {
        val challengeId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val error = RuntimeException("Database error")

        every {
            submissionRepository.findByUserIdAndChallengeId(userId, challengeId)
        } throws error

        val result = service.execute(challengeId, userId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { submissionRepository.findByUserIdAndChallengeId(userId, challengeId) }
    }
}
