package com.example.application.submission.service

import com.example.application.submission.model.SubmissionModel
import com.example.application.submission.repository.SubmissionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

class GetSubmissionByIdServiceTest {
    private val submissionRepository: SubmissionRepository = mockk()

    private val service = GetSubmissionByIdService(submissionRepository)

    @Test
    fun `should get submission by id successfully`() {
        val submissionId = UUID.randomUUID()
        val submissionModel = SubmissionModel(
            id = submissionId,
            userId = UUID.randomUUID(),
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "fun main() { println(\"Hello\") }",
            language = "kotlin",
            submittedAt = LocalDateTime.now()
        )

        every { submissionRepository.findById(submissionId) } returns Optional.of(submissionModel)

        val result = service.execute(submissionId)

        Assertions.assertTrue(result.isSuccess)
        val submission = result.getOrNull()
        Assertions.assertNotNull(submission)
        Assertions.assertEquals(submissionModel.id, submission?.id)
        Assertions.assertEquals(submissionModel.userId, submission?.userId)
        Assertions.assertEquals(submissionModel.challengeId, submission?.challengeId)
        Assertions.assertEquals(submissionModel.code, submission?.code)
        verify { submissionRepository.findById(submissionId) }
    }

    @Test
    fun `should fail when submission not found`() {
        val submissionId = UUID.randomUUID()

        every { submissionRepository.findById(submissionId) } returns Optional.empty()

        val result = service.execute(submissionId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is NoSuchElementException)
        Assertions.assertEquals("Submission not found", result.exceptionOrNull()?.message)
        verify { submissionRepository.findById(submissionId) }
    }

    @Test
    fun `should propagate repository error`() {
        val submissionId = UUID.randomUUID()
        val error = RuntimeException("Database error")

        every { submissionRepository.findById(submissionId) } throws error

        val result = service.execute(submissionId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { submissionRepository.findById(submissionId) }
    }
}
