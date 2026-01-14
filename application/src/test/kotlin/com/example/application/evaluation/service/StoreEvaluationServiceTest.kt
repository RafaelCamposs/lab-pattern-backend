package com.example.application.evaluation.service

import com.example.application.evaluation.model.EvaluationModel
import com.example.application.evaluation.repository.EvaluationRepository
import com.example.domain.evaluation.entity.dto.AiEvaluationDto
import com.example.domain.evaluation.entity.dto.StoreEvaluationDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class StoreEvaluationServiceTest {
    private val evaluationRepository: EvaluationRepository = mockk()

    private val service = StoreEvaluationService(evaluationRepository)

    @Test
    fun `should store evaluation successfully`() {
        val aiEvaluationDto = AiEvaluationDto(
            score = 85,
            feedback = listOf("Good implementation"),
            strengths = listOf("Clean code structure"),
            improvements = listOf("Could improve error handling")
        )

        val storeEvaluationDto = StoreEvaluationDto(
            submissionId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            aiEvaluationDto = aiEvaluationDto
        )

        val savedEvaluationModel = EvaluationModel(
            id = UUID.randomUUID(),
            submissionId = storeEvaluationDto.submissionId,
            score = aiEvaluationDto.score,
            feedback = aiEvaluationDto.getFeedback(),
            evaluatedAt = LocalDateTime.now()
        )

        val evaluationModelSlot = slot<EvaluationModel>()

        every { evaluationRepository.save(capture(evaluationModelSlot)) } returns savedEvaluationModel

        val result = service.execute(storeEvaluationDto)

        Assertions.assertTrue(result.isSuccess)
        val evaluation = result.getOrNull()
        Assertions.assertNotNull(evaluation)
        Assertions.assertEquals(savedEvaluationModel.id, evaluation?.id)
        Assertions.assertEquals(storeEvaluationDto.submissionId, evaluation?.submissionId)
        Assertions.assertEquals(aiEvaluationDto.score, evaluation?.score)
        Assertions.assertEquals(aiEvaluationDto.getFeedback(), evaluation?.feedback)

        verify { evaluationRepository.save(any()) }

        Assertions.assertEquals(storeEvaluationDto.submissionId, evaluationModelSlot.captured.submissionId)
        Assertions.assertEquals(aiEvaluationDto.score, evaluationModelSlot.captured.score)
    }

    @Test
    fun `should propagate repository error`() {
        val aiEvaluationDto = AiEvaluationDto(
            score = 85,
            feedback = listOf("Good implementation"),
            strengths = listOf("Clean code structure"),
            improvements = listOf("Could improve error handling")
        )

        val storeEvaluationDto = StoreEvaluationDto(
            submissionId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            aiEvaluationDto = aiEvaluationDto
        )

        val error = RuntimeException("Database error")

        every { evaluationRepository.save(any()) } throws error

        val result = service.execute(storeEvaluationDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { evaluationRepository.save(any()) }
    }
}
