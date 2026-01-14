package com.example.application.submission.service

import com.example.application.submission.model.SubmissionModel
import com.example.application.submission.repository.SubmissionRepository
import com.example.domain.submission.entity.dto.StoreSubmissionDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class StoreSubmissionServiceTest {
    private val submissionRepository: SubmissionRepository = mockk()

    private val service = StoreSubmissionService(submissionRepository)

    @Test
    fun `should store submission successfully`() {
        val storeSubmissionDto = StoreSubmissionDto(
            userId = UUID.randomUUID(),
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "fun main() { println(\"Hello\") }",
            language = "kotlin"
        )

        val savedSubmissionModel = SubmissionModel(
            id = UUID.randomUUID(),
            userId = storeSubmissionDto.userId,
            challengeId = storeSubmissionDto.challengeId,
            patternId = storeSubmissionDto.patternId,
            code = storeSubmissionDto.code,
            language = storeSubmissionDto.language,
            submittedAt = LocalDateTime.now()
        )

        val submissionModelSlot = slot<SubmissionModel>()

        every { submissionRepository.save(capture(submissionModelSlot)) } returns savedSubmissionModel

        val result = service.execute(storeSubmissionDto)

        Assertions.assertTrue(result.isSuccess)
        val submission = result.getOrNull()
        Assertions.assertNotNull(submission)
        Assertions.assertEquals(savedSubmissionModel.id, submission?.id)
        Assertions.assertEquals(storeSubmissionDto.userId, submission?.userId)
        Assertions.assertEquals(storeSubmissionDto.challengeId, submission?.challengeId)
        Assertions.assertEquals(storeSubmissionDto.patternId, submission?.patternId)
        Assertions.assertEquals(storeSubmissionDto.code, submission?.code)
        Assertions.assertEquals(storeSubmissionDto.language, submission?.language)

        verify { submissionRepository.save(any()) }

        Assertions.assertEquals(storeSubmissionDto.userId, submissionModelSlot.captured.userId)
        Assertions.assertEquals(storeSubmissionDto.challengeId, submissionModelSlot.captured.challengeId)
        Assertions.assertEquals(storeSubmissionDto.code, submissionModelSlot.captured.code)
    }

    @Test
    fun `should propagate repository error`() {
        val storeSubmissionDto = StoreSubmissionDto(
            userId = UUID.randomUUID(),
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "fun main() { println(\"Hello\") }",
            language = "kotlin"
        )

        val error = RuntimeException("Database error")

        every { submissionRepository.save(any()) } throws error

        val result = service.execute(storeSubmissionDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { submissionRepository.save(any()) }
    }
}
