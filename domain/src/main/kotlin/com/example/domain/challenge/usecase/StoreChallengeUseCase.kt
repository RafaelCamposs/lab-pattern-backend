package com.example.domain.challenge.usecase

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.entity.dto.StoreChallengeDto
import com.example.domain.challenge.gateway.StoreChallengeGateway
import jakarta.inject.Named

@Named
class StoreChallengeUseCase (
    private val storeChallengeGateway: StoreChallengeGateway,
) {
    fun execute(storeChallengeDto: StoreChallengeDto): Result<Challenge> {
        return storeChallengeGateway.execute(storeChallengeDto)
    }
}