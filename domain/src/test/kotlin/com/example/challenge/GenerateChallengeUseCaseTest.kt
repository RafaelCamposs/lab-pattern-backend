package com.example.challenge

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.entity.dto.AiQuestionDto
import com.example.domain.challenge.gateway.GetAiQuestionGateway
import com.example.domain.challenge.gateway.StoreChallengeGateway
import com.example.domain.challenge.usecase.GenerateChallengeUseCase
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.usecase.GetRandomPatternUseCase
import com.example.domain.pattern.usecase.GetRandomThemeUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GenerateChallengeUseCaseTest {
    private val getAiQuestionGateway: GetAiQuestionGateway = mockk()
    private val storeChallengeGateway: StoreChallengeGateway = mockk()
    private val getRandomPatternUseCase: GetRandomPatternUseCase = mockk()
    private val getRandomThemeUseCase: GetRandomThemeUseCase = mockk()

    private val useCase = GenerateChallengeUseCase(
        getAiQuestionGateway,
        storeChallengeGateway,
        getRandomPatternUseCase,
        getRandomThemeUseCase
    )

    @Test
    fun `should generate challenge successfully`() {
        val pattern = DesignPattern(
            id = UUID.randomUUID(),
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )
        val theme = "E-commerce"
        val aiResponse = AiQuestionDto(
            title = "Shopping Cart Discount",
            description = "Implement a shopping cart with different discount strategies"
        )
        val challenge = Challenge(
            id = UUID.randomUUID(),
            expectedPatternId = pattern.id,
            title = aiResponse.title,
            description = aiResponse.description,
            createdAt = LocalDateTime.now(),
            publishedAt = LocalDateTime.now()
        )

        every { getRandomPatternUseCase.execute() } returns Result.success(pattern)
        every { getRandomThemeUseCase.execute() } returns Result.success(theme)
        every { getAiQuestionGateway.execute(pattern, theme) } returns Result.success(aiResponse)
        every {
            storeChallengeGateway.execute(match {
                it.expectedPatternId == pattern.id &&
                        it.title == aiResponse.title &&
                        it.description == aiResponse.description
            })
        } returns Result.success(challenge)

        val result = useCase.execute()

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(challenge, result.getOrNull())
        verify {
            getRandomPatternUseCase.execute()
            getRandomThemeUseCase.execute()
            getAiQuestionGateway.execute(pattern, theme)
            storeChallengeGateway.execute(any())
        }
    }

    @Test
    fun `should propagate pattern use case error`() {
        val error = RuntimeException("Pattern error")
        every { getRandomPatternUseCase.execute() } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertThrows(RuntimeException::class.java) { result.getOrThrow() }
    }

    @Test
    fun `should propagate theme use case error`() {
        val pattern = DesignPattern(
            id = UUID.randomUUID(),
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )
        val error = RuntimeException("Theme error")

        every { getRandomPatternUseCase.execute() } returns Result.success(pattern)
        every { getRandomThemeUseCase.execute() } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertThrows(RuntimeException::class.java) { result.getOrThrow() }
    }

    @Test
    fun `should propagate ai gateway error`() {
        val pattern = DesignPattern(
            id = UUID.randomUUID(),
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )
        val theme = "E-commerce"
        val error = RuntimeException("AI error")

        every { getRandomPatternUseCase.execute() } returns Result.success(pattern)
        every { getRandomThemeUseCase.execute() } returns Result.success(theme)
        every { getAiQuestionGateway.execute(pattern, theme) } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertThrows(RuntimeException::class.java) { result.getOrThrow() }
    }

    @Test
    fun `should propagate storage gateway error`() {
        val pattern = DesignPattern(
            id = UUID.randomUUID(),
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )
        val theme = "E-commerce"
        val aiResponse = AiQuestionDto(
            title = "Shopping Cart Discount",
            description = "Implement a shopping cart with different discount strategies"
        )
        val error = RuntimeException("Storage error")

        every { getRandomPatternUseCase.execute() } returns Result.success(pattern)
        every { getRandomThemeUseCase.execute() } returns Result.success(theme)
        every { getAiQuestionGateway.execute(pattern, theme) } returns Result.success(aiResponse)
        every { storeChallengeGateway.execute(any()) } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
    }
}