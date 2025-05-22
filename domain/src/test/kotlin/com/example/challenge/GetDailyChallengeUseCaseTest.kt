package com.example.domain.challenge.usecase

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetLatestDailyChallengeGateway
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetDailyChallengeUseCaseTest {
    private val getLatestDailyChallengeGateway: GetLatestDailyChallengeGateway = mockk()
    private val useCase = GetDailyChallengeUseCase(getLatestDailyChallengeGateway)

    @Test
    fun `should return daily challenge successfully`() {
        val challenge = Challenge(
            id = UUID.randomUUID(),
            expectedPatternId = UUID.randomUUID(),
            title = "Daily Challenge Title",
            description = "Daily Challenge Description",
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )
        every { getLatestDailyChallengeGateway.execute() } returns Result.success(challenge)

        val result = useCase.execute()

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(challenge, result.getOrNull())
        verify { getLatestDailyChallengeGateway.execute() }
    }

    @Test
    fun `should propagate gateway error`() {
        val error = RuntimeException("Gateway error")
        every { getLatestDailyChallengeGateway.execute() } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { getLatestDailyChallengeGateway.execute() }
    }
}