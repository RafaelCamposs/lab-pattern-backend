package com.example.pattern

import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.gateway.GetAllPatternsGateway
import com.example.domain.pattern.usecase.GetAllPatternsUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class GetAllPatternsUseCaseTest {
    private val getAllPatternsGateway: GetAllPatternsGateway = mockk()
    private val useCase = GetAllPatternsUseCase(getAllPatternsGateway)

    @Test
    fun `should return all patterns successfully`() {
        val patterns = listOf(
            DesignPattern(
                id = UUID.randomUUID(),
                name = "Abstract Factory",
                category = "Creational",
                description = "desc1"
            ),
            DesignPattern(id = UUID.randomUUID(), name = "Observer", category = "Behavioral", description = "desc2")
        )
        every { getAllPatternsGateway.execute() } returns Result.success(patterns)

        val result = useCase.execute()

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(patterns, result.getOrNull())
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