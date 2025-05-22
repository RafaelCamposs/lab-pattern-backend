package com.example.pattern

import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.gateway.GetAllPatternsGateway
import com.example.domain.pattern.usecase.GetRandomPatternUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class GetRandomPatternUseCaseTest {
    private val getAllPatternsGateway: GetAllPatternsGateway = mockk()
    private val useCase = GetRandomPatternUseCase(getAllPatternsGateway)

    @Test
    fun `should return random pattern from list`() {
        val patterns = listOf(
            DesignPattern(
                id = UUID.randomUUID(),
                name = "Abstract Factory",
                category = "Creational",
                description = "desc1"
            ),
            DesignPattern(id = UUID.randomUUID(), name = "Factory", category = "Creational", description = "desc2")
        )
        every { getAllPatternsGateway.execute() } returns Result.success(patterns)

        val result = useCase.execute()

        Assertions.assertTrue(result.isSuccess)
        result.getOrNull()?.let { pattern ->
            Assertions.assertTrue(patterns.contains(pattern))
        }
    }

    @Test
    fun `should propagate gateway error`() {
        val error = RuntimeException("Gateway error")
        every { getAllPatternsGateway.execute() } returns Result.failure(error)

        val result = useCase.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
    }
}