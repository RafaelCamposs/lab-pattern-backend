package com.example.application.challenge.service

import com.example.application.challenge.repository.ChallengeRepository
import com.example.domain.challenge.gateway.CountUserSolvedChallengesGateway
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CountUserSolvedChallengesService (
    private val challengeRepository: ChallengeRepository
): CountUserSolvedChallengesGateway {
    override fun execute(userId: UUID): Result<Int> {
        return runCatching {
            challengeRepository.countByUserIdWithSubmission(userId)
        }
    }
}