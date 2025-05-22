package com.example.challenge

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.entity.dto.StoreChallengeDto
import com.example.domain.challenge.gateway.StoreChallengeGateway
import com.example.domain.challenge.usecase.StoreChallengeUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class StoreChallengeUseCaseTest {
    private val storeChallengeGateway: StoreChallengeGateway = mockk()
    private val useCase = StoreChallengeUseCase(storeChallengeGateway)

    @Test
    fun `should store challenge successfully`() {
        val dto = StoreChallengeDto(
            expectedPatternId = UUID.randomUUID(),
            title = "Challenge Title",
            description = "Challenge Description"
        )
        val challenge = Challenge(
            id = UUID.randomUUID(),
            expectedPatternId = dto.expectedPatternId,
            title = dto.title,
            description = dto.description,
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )
        every { storeChallengeGateway.execute(dto) } returns Result.success(challenge)

        val result = useCase.execute(dto)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(challenge, result.getOrNull())
        verify { storeChallengeGateway.execute(dto) }
    }

    @Test
    fun `should propagate gateway error`() {
        val dto = StoreChallengeDto(
            expectedPatternId = UUID.randomUUID(),
            title = "Challenge Title",
            description = "Challenge Description"
        )
        val error = RuntimeException("Gateway error")
        every { storeChallengeGateway.execute(dto) } returns Result.failure(error)

        val result = useCase.execute(dto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { storeChallengeGateway.execute(dto) }
    }
}