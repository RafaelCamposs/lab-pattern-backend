package com.example.application.pattern.service

import com.example.application.pattern.model.DesignPatternModel
import com.example.application.pattern.repository.DesignPatternRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class GetAllPatternsServiceTest {
    private val designPatternRepository: DesignPatternRepository = mockk()

    private val service = GetAllPatternsService(designPatternRepository)

    @Test
    fun `should get all patterns successfully`() {
        val patternModels = listOf(
            DesignPatternModel(
                id = UUID.randomUUID(),
                name = "Strategy",
                description = "Strategy pattern description",
                category = "Behavioral"
            ),
            DesignPatternModel(
                id = UUID.randomUUID(),
                name = "Factory",
                description = "Factory pattern description",
                category = "Creational"
            )
        )

        every { designPatternRepository.findAll() } returns patternModels

        val result = service.execute()

        Assertions.assertTrue(result.isSuccess)
        val patterns = result.getOrNull()
        Assertions.assertNotNull(patterns)
        Assertions.assertEquals(2, patterns?.size)
        Assertions.assertEquals(patternModels[0].id, patterns?.get(0)?.id)
        Assertions.assertEquals(patternModels[1].id, patterns?.get(1)?.id)
        verify { designPatternRepository.findAll() }
    }

    @Test
    fun `should return empty list when no patterns exist`() {
        every { designPatternRepository.findAll() } returns emptyList()

        val result = service.execute()

        Assertions.assertTrue(result.isSuccess)
        val patterns = result.getOrNull()
        Assertions.assertNotNull(patterns)
        Assertions.assertEquals(0, patterns?.size)
        verify { designPatternRepository.findAll() }
    }

    @Test
    fun `should propagate repository error`() {
        val error = RuntimeException("Database error")

        every { designPatternRepository.findAll() } throws error

        val result = service.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { designPatternRepository.findAll() }
    }
}
