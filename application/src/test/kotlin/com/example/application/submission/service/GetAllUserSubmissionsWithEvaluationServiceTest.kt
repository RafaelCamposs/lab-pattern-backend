package com.example.application.submission.service

import com.example.application.submission.repository.SubmissionRepository
import com.example.application.submission.repository.projections.SubmissionWithEvaluationDto
import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.Feedback
import com.example.domain.submission.entity.Submission
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetAllUserSubmissionsWithEvaluationServiceTest {
    private val submissionRepository: SubmissionRepository = mockk()

    private val service = GetAllUserSubmissionsWithEvaluationService(submissionRepository)

    @Test
    fun `should get all user submissions with evaluation successfully`() {
        val userId = UUID.randomUUID()

        val submissionWithEvaluationDto1 = mockk<SubmissionWithEvaluationDto>()
        val submissionWithEvaluationDto2 = mockk<SubmissionWithEvaluationDto>()

        val submission1 = Submission(
            id = UUID.randomUUID(),
            userId = userId,
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code1",
            language = "kotlin",
            submittedAt = LocalDateTime.now(),
            evaluation = Evaluation(
                id = UUID.randomUUID(),
                submissionId = UUID.randomUUID(),
                score = 85,
                feedback = Feedback(
                    content = listOf("Good"),
                    improvements = listOf("Could improve"),
                    strengths = listOf("Clean code")
                ),
                evaluatedAt = LocalDateTime.now()
            )
        )

        val submission2 = Submission(
            id = UUID.randomUUID(),
            userId = userId,
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code2",
            language = "kotlin",
            submittedAt = LocalDateTime.now(),
            evaluation = null
        )

        val dtoList = listOf(submissionWithEvaluationDto1, submissionWithEvaluationDto2)

        mockkStatic(SubmissionWithEvaluationDto::class)
        every { submissionRepository.findAllByUserWithEvaluation(userId) } returns dtoList
        every { SubmissionWithEvaluationDto.toDomain(submissionWithEvaluationDto1) } returns submission1
        every { SubmissionWithEvaluationDto.toDomain(submissionWithEvaluationDto2) } returns submission2

        val result = service.execute(userId)

        Assertions.assertTrue(result.isSuccess)
        val submissions = result.getOrNull()
        Assertions.assertNotNull(submissions)
        Assertions.assertEquals(2, submissions?.size)
        Assertions.assertEquals(submission1, submissions?.get(0))
        Assertions.assertEquals(submission2, submissions?.get(1))
        verify { submissionRepository.findAllByUserWithEvaluation(userId) }
    }

    @Test
    fun `should return empty list when user has no submissions`() {
        val userId = UUID.randomUUID()

        every { submissionRepository.findAllByUserWithEvaluation(userId) } returns emptyList()

        val result = service.execute(userId)

        Assertions.assertTrue(result.isSuccess)
        val submissions = result.getOrNull()
        Assertions.assertNotNull(submissions)
        Assertions.assertEquals(0, submissions?.size)
        verify { submissionRepository.findAllByUserWithEvaluation(userId) }
    }

    @Test
    fun `should propagate repository error`() {
        val userId = UUID.randomUUID()
        val error = RuntimeException("Database error")

        every { submissionRepository.findAllByUserWithEvaluation(userId) } throws error

        val result = service.execute(userId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { submissionRepository.findAllByUserWithEvaluation(userId) }
    }
}
