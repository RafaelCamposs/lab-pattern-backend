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
    private val getAllPatternsGateway: com.example.domain.pattern.gateway.GetAllPatternsGateway = mockk()
    private val getRandomPatternUseCase: GetRandomPatternUseCase = GetRandomPatternUseCase(getAllPatternsGateway)
    private val getRandomThemeUseCase: GetRandomThemeUseCase = GetRandomThemeUseCase()

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

        every { getAllPatternsGateway.execute() } returns Result.success(listOf(pattern))
        every { getAiQuestionGateway.execute(pattern, any()) } returns Result.success(aiResponse)
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
            getAllPatternsGateway.execute()
            getAiQuestionGateway.execute(pattern, any())
            storeChallengeGateway.execute(any())
        }
    }

    @Test
    fun `should propagate pattern use case error`() {
        val error = RuntimeException("Pattern error")
        every { getAllPatternsGateway.execute() } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `should propagate ai gateway error`() {
        val pattern = DesignPattern(
            id = UUID.randomUUID(),
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )
        val error = RuntimeException("AI error")

        every { getAllPatternsGateway.execute() } returns Result.success(listOf(pattern))
        every { getAiQuestionGateway.execute(pattern, any()) } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `should propagate storage gateway error`() {
        val pattern = DesignPattern(
            id = UUID.randomUUID(),
            name = "Strategy",
            category = "Behavioral",
            description = "Strategy pattern description"
        )
        val aiResponse = AiQuestionDto(
            title = "Shopping Cart Discount",
            description = "Implement a shopping cart with different discount strategies"
        )
        val error = RuntimeException("Storage error")

        every { getAllPatternsGateway.execute() } returns Result.success(listOf(pattern))
        every { getAiQuestionGateway.execute(pattern, any()) } returns Result.success(aiResponse)
        every { storeChallengeGateway.execute(any()) } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
    }
}