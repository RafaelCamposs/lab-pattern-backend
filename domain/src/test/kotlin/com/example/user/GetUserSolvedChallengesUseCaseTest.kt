package com.example.user

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetUserSolvedChallengesGateway
import com.example.domain.common.Page
import com.example.domain.user.usecase.GetUserSolvedChallengesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetUserSolvedChallengesUseCaseTest {
    private val getUserSolvedChallengesGateway: GetUserSolvedChallengesGateway = mockk()

    private val useCase = GetUserSolvedChallengesUseCase(getUserSolvedChallengesGateway)

    @Test
    fun `should get user solved challenges successfully`() {
        val userId = UUID.randomUUID()
        val page = 0
        val pageSize = 10

        val challenges = listOf(
            Challenge(
                id = UUID.randomUUID(),
                expectedPatternId = UUID.randomUUID(),
                title = "Challenge 1",
                description = "Description 1",
                createdAt = LocalDateTime.now(),
                publishedAt = LocalDateTime.now()
            ),
            Challenge(
                id = UUID.randomUUID(),
                expectedPatternId = UUID.randomUUID(),
                title = "Challenge 2",
                description = "Description 2",
                createdAt = LocalDateTime.now(),
                publishedAt = LocalDateTime.now()
            )
        )

        val challengePage = Page(
            content = challenges,
            totalPages = 1,
            totalElements = 2L,
            currentPage = page,
            pageSize = pageSize,
            isLast = true
        )

        every {
            getUserSolvedChallengesGateway.execute(userId, page, pageSize)
        } returns Result.success(challengePage)

        val result = useCase.execute(userId, page, pageSize)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(challengePage, result.getOrNull())
        verify { getUserSolvedChallengesGateway.execute(userId, page, pageSize) }
    }

    @Test
    fun `should propagate gateway error`() {
        val userId = UUID.randomUUID()
        val page = 0
        val pageSize = 10
        val error = RuntimeException("Gateway error")

        every {
            getUserSolvedChallengesGateway.execute(userId, page, pageSize)
        } returns Result.failure(error)

        val result = useCase.execute(userId, page, pageSize)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { getUserSolvedChallengesGateway.execute(userId, page, pageSize) }
    }
}
