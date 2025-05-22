package com.example.pattern

import com.example.domain.pattern.usecase.GetRandomThemeUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetRandomThemeUseCaseTest {
    private val useCase = GetRandomThemeUseCase()

    @Test
    fun `should return random theme successfully`() {
        val result = useCase.execute()

        Assertions.assertTrue(result.isSuccess)
        result.getOrNull()?.let { theme ->
            Assertions.assertTrue(theme in useCase.themes)
        }
    }

    @Test
    fun `should return different themes on multiple executions`() {
        val results = (1..10).mapNotNull { useCase.execute().getOrNull() }.toSet()

        Assertions.assertTrue(results.size > 1, "Expected different themes to be returned")
    }
}