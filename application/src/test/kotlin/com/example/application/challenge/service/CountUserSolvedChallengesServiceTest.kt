package com.example.application.challenge.service

import com.example.application.challenge.repository.ChallengeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class CountUserSolvedChallengesServiceTest {
    private val challengeRepository: ChallengeRepository = mockk()

    private val service = CountUserSolvedChallengesService(challengeRepository)

    @Test
    fun `should count user solved challenges successfully`() {
        val userId = UUID.randomUUID()
        val count = 5

        every { challengeRepository.countByUserIdWithSubmission(userId) } returns count

        val result = service.execute(userId)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(count, result.getOrNull())
        verify { challengeRepository.countByUserIdWithSubmission(userId) }
    }

    @Test
    fun `should return zero when user has no solved challenges`() {
        val userId = UUID.randomUUID()

        every { challengeRepository.countByUserIdWithSubmission(userId) } returns 0

        val result = service.execute(userId)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(0, result.getOrNull())
        verify { challengeRepository.countByUserIdWithSubmission(userId) }
    }

    @Test
    fun `should propagate repository error`() {
        val userId = UUID.randomUUID()
        val error = RuntimeException("Database error")

        every { challengeRepository.countByUserIdWithSubmission(userId) } throws error

        val result = service.execute(userId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { challengeRepository.countByUserIdWithSubmission(userId) }
    }
}
