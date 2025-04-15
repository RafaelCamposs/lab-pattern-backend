package com.example.application.challenge.service

import com.example.application.challenge.model.ChallengeModel
import com.example.application.challenge.repository.ChallengeRepository
import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.entity.dto.StoreChallengeDto
import com.example.domain.challenge.gateway.StoreChallengeGateway
import org.springframework.stereotype.Component

@Component
class StoreChallengeService (
    private val challengeRepository: ChallengeRepository
) : StoreChallengeGateway {
    override fun execute(storeChallengeDto: StoreChallengeDto): Result<Challenge> {
        return kotlin.runCatching {
            val challengeModel = ChallengeModel.fromStoreChallengeDto(storeChallengeDto)
            challengeRepository.save(challengeModel).toDomain()
        }
    }
}