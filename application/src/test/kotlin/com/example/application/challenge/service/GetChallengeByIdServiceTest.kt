package com.example.application.challenge.service

import com.example.application.challenge.model.ChallengeModel
import com.example.application.challenge.repository.ChallengeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

class GetChallengeByIdServiceTest {
    private val challengeRepository: ChallengeRepository = mockk()

    private val service = GetChallengeByIdService(challengeRepository)

    @Test
    fun `should get challenge by id successfully`() {
        val challengeId = UUID.randomUUID()
        val challengeModel = ChallengeModel(
            id = challengeId,
            title = "Design Pattern Challenge",
            description = "Implement a strategy pattern",
            expectedPatternId = UUID.randomUUID(),
            publishedAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now(),
            isDaily = true
        )

        every { challengeRepository.findById(challengeId) } returns Optional.of(challengeModel)

        val result = service.execute(challengeId)

        Assertions.assertTrue(result.isSuccess)
        val challenge = result.getOrNull()
        Assertions.assertNotNull(challenge)
        Assertions.assertEquals(challengeModel.id, challenge?.id)
        Assertions.assertEquals(challengeModel.title, challenge?.title)
        Assertions.assertEquals(challengeModel.description, challenge?.description)
        verify { challengeRepository.findById(challengeId) }
    }

    @Test
    fun `should fail when challenge not found`() {
        val challengeId = UUID.randomUUID()

        every { challengeRepository.findById(challengeId) } returns Optional.empty()

        val result = service.execute(challengeId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is NoSuchElementException)
        Assertions.assertEquals("Challenge not found", result.exceptionOrNull()?.message)
        verify { challengeRepository.findById(challengeId) }
    }

    @Test
    fun `should propagate repository error`() {
        val challengeId = UUID.randomUUID()
        val error = RuntimeException("Database error")

        every { challengeRepository.findById(challengeId) } throws error

        val result = service.execute(challengeId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { challengeRepository.findById(challengeId) }
    }
}
