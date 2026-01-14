package com.example.application.challenge.service

import com.example.application.challenge.model.ChallengeModel
import com.example.application.challenge.repository.ChallengeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetLatestDailyChallengeServiceTest {
    private val challengeRepository: ChallengeRepository = mockk()

    private val service = GetLatestDailyChallengeService(challengeRepository)

    @Test
    fun `should get latest daily challenge successfully`() {
        val challengeModel = ChallengeModel(
            id = UUID.randomUUID(),
            title = "Today's Challenge",
            description = "Implement a factory pattern",
            expectedPatternId = UUID.randomUUID(),
            publishedAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now(),
            isDaily = true
        )

        every { challengeRepository.findLatestDailyChallenge() } returns challengeModel

        val result = service.execute()

        Assertions.assertTrue(result.isSuccess)
        val challenge = result.getOrNull()
        Assertions.assertNotNull(challenge)
        Assertions.assertEquals(challengeModel.id, challenge?.id)
        Assertions.assertEquals(challengeModel.title, challenge?.title)
        Assertions.assertEquals(challengeModel.description, challenge?.description)
        Assertions.assertTrue(challenge?.isDaily == true)
        verify { challengeRepository.findLatestDailyChallenge() }
    }

    @Test
    fun `should propagate repository error`() {
        val error = RuntimeException("Database error")

        every { challengeRepository.findLatestDailyChallenge() } throws error

        val result = service.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { challengeRepository.findLatestDailyChallenge() }
    }
}
