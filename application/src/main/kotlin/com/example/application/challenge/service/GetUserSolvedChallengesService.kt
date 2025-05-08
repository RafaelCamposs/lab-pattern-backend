package com.example.application.challenge.service

import com.example.application.challenge.model.ChallengeModel
import com.example.application.challenge.repository.ChallengeRepository
import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetUserSolvedChallengesGateway
import com.example.domain.common.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetUserSolvedChallengesService (
    private val challengeRepository: ChallengeRepository
): GetUserSolvedChallengesGateway {
    override fun execute(
        id: UUID,
        page: Int,
        pageSize: Int
    ): Result<Page<Challenge>> {
        return runCatching {
            val pageable = createPageable(page, pageSize)

            val challengePage = challengeRepository.findByUserIdWithSubmission(id, pageable)

            createChallengePage(challengePage)
        }
    }

    private fun createChallengePage(challengePage: org.springframework.data.domain.Page<ChallengeModel>): Page<Challenge> = Page(
        content = challengePage.content.map { it.toDomain() },
        totalElements = challengePage.totalElements,
        totalPages = challengePage.totalPages,
        currentPage = challengePage.number,
        pageSize = challengePage.size,
        isLast = challengePage.isLast
    )

    private fun createPageable(page: Int, pageSize: Int): PageRequest = PageRequest.of(
        page,
        pageSize,
        Sort.by(Sort.Direction.DESC, "published_at")
    )

}