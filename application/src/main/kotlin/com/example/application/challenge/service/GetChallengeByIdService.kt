package com.example.application.challenge.service

import com.example.application.challenge.repository.ChallengeRepository
import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetChallengeByIdGateway

import org.springframework.stereotype.Component
import java.util.*
import kotlin.NoSuchElementException

@Component
class GetChallengeByIdService (
    private val challengeRepository: ChallengeRepository,
): GetChallengeByIdGateway {
    override fun execute(id: UUID): Result<Challenge> {
        return runCatching {
            challengeRepository.findById(id)
                .orElseThrow { NoSuchElementException("Challenge not found") }
                .toDomain()
        }
    }
}