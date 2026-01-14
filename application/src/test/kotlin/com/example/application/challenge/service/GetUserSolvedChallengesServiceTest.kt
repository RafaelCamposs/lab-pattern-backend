package com.example.application.challenge.service

import com.example.application.challenge.model.ChallengeModel
import com.example.application.challenge.repository.ChallengeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.UUID

class GetUserSolvedChallengesServiceTest {
    private val challengeRepository: ChallengeRepository = mockk()

    private val service = GetUserSolvedChallengesService(challengeRepository)

    @Test
    fun `should get user solved challenges successfully`() {
        val userId = UUID.randomUUID()
        val page = 0
        val pageSize = 10

        val challengeModels = listOf(
            ChallengeModel(
                id = UUID.randomUUID(),
                title = "Challenge 1",
                description = "Description 1",
                expectedPatternId = UUID.randomUUID(),
                publishedAt = LocalDateTime.now(),
                createdAt = LocalDateTime.now(),
                isDaily = true
            ),
            ChallengeModel(
                id = UUID.randomUUID(),
                title = "Challenge 2",
                description = "Description 2",
                expectedPatternId = UUID.randomUUID(),
                publishedAt = LocalDateTime.now(),
                createdAt = LocalDateTime.now(),
                isDaily = false
            )
        )

        val pageable = PageRequest.of(page, pageSize)
        val challengePage = PageImpl(challengeModels, pageable, challengeModels.size.toLong())

        every { challengeRepository.findByUserIdWithSubmission(userId, any()) } returns challengePage

        val result = service.execute(userId, page, pageSize)

        Assertions.assertTrue(result.isSuccess)
        val resultPage = result.getOrNull()
        Assertions.assertNotNull(resultPage)
        Assertions.assertEquals(2, resultPage?.content?.size)
        Assertions.assertEquals(challengeModels[0].id, resultPage?.content?.get(0)?.id)
        Assertions.assertEquals(challengeModels[1].id, resultPage?.content?.get(1)?.id)
        Assertions.assertEquals(page, resultPage?.currentPage)
        Assertions.assertEquals(pageSize, resultPage?.pageSize)
        verify { challengeRepository.findByUserIdWithSubmission(userId, any()) }
    }

    @Test
    fun `should return empty page when user has no solved challenges`() {
        val userId = UUID.randomUUID()
        val page = 0
        val pageSize = 10

        val pageable = PageRequest.of(page, pageSize)
        val emptyPage = PageImpl<ChallengeModel>(emptyList(), pageable, 0)

        every { challengeRepository.findByUserIdWithSubmission(userId, any()) } returns emptyPage

        val result = service.execute(userId, page, pageSize)

        Assertions.assertTrue(result.isSuccess)
        val resultPage = result.getOrNull()
        Assertions.assertNotNull(resultPage)
        Assertions.assertEquals(0, resultPage?.content?.size)
        Assertions.assertTrue(resultPage?.isLast == true)
        verify { challengeRepository.findByUserIdWithSubmission(userId, any()) }
    }

    @Test
    fun `should propagate repository error`() {
        val userId = UUID.randomUUID()
        val page = 0
        val pageSize = 10
        val error = RuntimeException("Database error")

        every { challengeRepository.findByUserIdWithSubmission(userId, any()) } throws error

        val result = service.execute(userId, page, pageSize)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { challengeRepository.findByUserIdWithSubmission(userId, any()) }
    }
}
