package com.example.application.evaluation.service

import com.example.application.evaluation.model.EvaluationModel
import com.example.application.evaluation.repository.EvaluationRepository
import com.example.domain.evaluation.entity.Feedback
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetEvaluationsBySubmissionIdsServiceTest {
    private val evaluationRepository: EvaluationRepository = mockk()

    private val service = GetEvaluationsBySubmissionIdsService(evaluationRepository)

    @Test
    fun `should get evaluations by submission ids successfully`() {
        val submissionId1 = UUID.randomUUID()
        val submissionId2 = UUID.randomUUID()
        val submissionIds = listOf(submissionId1, submissionId2)

        val evaluationModels = listOf(
            EvaluationModel(
                id = UUID.randomUUID(),
                submissionId = submissionId1,
                score = 85,
                feedback = Feedback(
                    content = listOf("Good"),
                    improvements = listOf("Could improve"),
                    strengths = listOf("Clean code")
                ),
                evaluatedAt = LocalDateTime.now()
            ),
            EvaluationModel(
                id = UUID.randomUUID(),
                submissionId = submissionId2,
                score = 90,
                feedback = Feedback(
                    content = listOf("Excellent"),
                    improvements = listOf("Minor issues"),
                    strengths = listOf("Great structure")
                ),
                evaluatedAt = LocalDateTime.now()
            )
        )

        every { evaluationRepository.findBySubmissionIds(submissionIds) } returns evaluationModels

        val result = service.execute(submissionIds)

        Assertions.assertTrue(result.isSuccess)
        val evaluations = result.getOrNull()
        Assertions.assertNotNull(evaluations)
        Assertions.assertEquals(2, evaluations?.size)
        Assertions.assertEquals(evaluationModels[0].id, evaluations?.get(0)?.id)
        Assertions.assertEquals(evaluationModels[1].id, evaluations?.get(1)?.id)
        verify { evaluationRepository.findBySubmissionIds(submissionIds) }
    }

    @Test
    fun `should return empty list when no evaluations found`() {
        val submissionIds = listOf(UUID.randomUUID(), UUID.randomUUID())

        every { evaluationRepository.findBySubmissionIds(submissionIds) } returns emptyList()

        val result = service.execute(submissionIds)

        Assertions.assertTrue(result.isSuccess)
        val evaluations = result.getOrNull()
        Assertions.assertNotNull(evaluations)
        Assertions.assertEquals(0, evaluations?.size)
        verify { evaluationRepository.findBySubmissionIds(submissionIds) }
    }

    @Test
    fun `should propagate repository error`() {
        val submissionIds = listOf(UUID.randomUUID(), UUID.randomUUID())
        val error = RuntimeException("Database error")

        every { evaluationRepository.findBySubmissionIds(submissionIds) } throws error

        val result = service.execute(submissionIds)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { evaluationRepository.findBySubmissionIds(submissionIds) }
    }
}
