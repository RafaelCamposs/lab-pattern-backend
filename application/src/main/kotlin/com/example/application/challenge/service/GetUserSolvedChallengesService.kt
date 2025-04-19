package com.example.application.challenge.service

import com.example.application.challenge.repository.ChallengeRepository
import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetUserSolvedChallengesGateway
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetUserSolvedChallengesService (
    private val challengeRepository: ChallengeRepository
): GetUserSolvedChallengesGateway {
    override fun execute(id: UUID): Result<List<Challenge>> {
        return runCatching {
            challengeRepository.findByUserIdWithSubmission(id).map {
                it.toDomain()
            }
        }
    }

}