package com.example.application.challenge.service

import com.example.application.challenge.repository.ChallengeRepository
import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetLatestDailyChallengeGateway
import org.springframework.stereotype.Component

@Component
class GetLatestDailyChallengeService (
    private val challengeRepository: ChallengeRepository
): GetLatestDailyChallengeGateway {
    override fun execute(): Result<Challenge> {
        return runCatching {
            challengeRepository.findLatestDailyChallenge().toDomain()
        }
    }
}