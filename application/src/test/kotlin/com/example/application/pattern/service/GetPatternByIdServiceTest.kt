package com.example.application.pattern.service

import com.example.application.pattern.model.DesignPatternModel
import com.example.application.pattern.repository.DesignPatternRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

class GetPatternByIdServiceTest {
    private val patternRepository: DesignPatternRepository = mockk()

    private val service = GetPatternByIdService(patternRepository)

    @Test
    fun `should get pattern by id successfully`() {
        val patternId = UUID.randomUUID()
        val patternModel = DesignPatternModel(
            id = patternId,
            name = "Strategy",
            description = "Strategy pattern description",
            category = "Behavioral"
        )

        every { patternRepository.findById(patternId) } returns Optional.of(patternModel)

        val result = service.execute(patternId)

        Assertions.assertTrue(result.isSuccess)
        val pattern = result.getOrNull()
        Assertions.assertNotNull(pattern)
        Assertions.assertEquals(patternModel.id, pattern?.id)
        Assertions.assertEquals(patternModel.name, pattern?.name)
        Assertions.assertEquals(patternModel.description, pattern?.description)
        verify { patternRepository.findById(patternId) }
    }

    @Test
    fun `should fail when pattern not found`() {
        val patternId = UUID.randomUUID()

        every { patternRepository.findById(patternId) } returns Optional.empty()

        val result = service.execute(patternId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is NoSuchElementException)
        Assertions.assertEquals("Pattern not found", result.exceptionOrNull()?.message)
        verify { patternRepository.findById(patternId) }
    }

    @Test
    fun `should propagate repository error`() {
        val patternId = UUID.randomUUID()
        val error = RuntimeException("Database error")

        every { patternRepository.findById(patternId) } throws error

        val result = service.execute(patternId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { patternRepository.findById(patternId) }
    }
}
