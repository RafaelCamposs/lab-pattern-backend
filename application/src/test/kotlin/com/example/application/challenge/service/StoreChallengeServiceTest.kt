package com.example.application.challenge.service

import com.example.application.challenge.model.ChallengeModel
import com.example.application.challenge.repository.ChallengeRepository
import com.example.domain.challenge.entity.dto.StoreChallengeDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class StoreChallengeServiceTest {
    private val challengeRepository: ChallengeRepository = mockk()

    private val service = StoreChallengeService(challengeRepository)

    @Test
    fun `should store challenge successfully`() {
        val storeChallengeDto = StoreChallengeDto(
            title = "Design Pattern Challenge",
            description = "Implement a strategy pattern",
            expectedPatternId = UUID.randomUUID(),
            publishedAt = LocalDateTime.now(),
            isDaily = true
        )

        val savedChallengeModel = ChallengeModel(
            id = UUID.randomUUID(),
            title = storeChallengeDto.title,
            description = storeChallengeDto.description,
            expectedPatternId = storeChallengeDto.expectedPatternId,
            publishedAt = storeChallengeDto.publishedAt,
            createdAt = LocalDateTime.now(),
            isDaily = storeChallengeDto.isDaily
        )

        val challengeModelSlot = slot<ChallengeModel>()

        every { challengeRepository.save(capture(challengeModelSlot)) } returns savedChallengeModel

        val result = service.execute(storeChallengeDto)

        Assertions.assertTrue(result.isSuccess)
        val challenge = result.getOrNull()
        Assertions.assertNotNull(challenge)
        Assertions.assertEquals(savedChallengeModel.id, challenge?.id)
        Assertions.assertEquals(storeChallengeDto.title, challenge?.title)
        Assertions.assertEquals(storeChallengeDto.description, challenge?.description)
        Assertions.assertEquals(storeChallengeDto.expectedPatternId, challenge?.expectedPatternId)

        verify { challengeRepository.save(any()) }

        Assertions.assertEquals(storeChallengeDto.title, challengeModelSlot.captured.title)
        Assertions.assertEquals(storeChallengeDto.description, challengeModelSlot.captured.description)
        Assertions.assertEquals(storeChallengeDto.expectedPatternId, challengeModelSlot.captured.expectedPatternId)
    }

    @Test
    fun `should propagate repository error`() {
        val storeChallengeDto = StoreChallengeDto(
            title = "Design Pattern Challenge",
            description = "Implement a strategy pattern",
            expectedPatternId = UUID.randomUUID(),
            publishedAt = LocalDateTime.now(),
            isDaily = true
        )

        val error = RuntimeException("Database error")

        every { challengeRepository.save(any()) } throws error

        val result = service.execute(storeChallengeDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { challengeRepository.save(any()) }
    }
}
