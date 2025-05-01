package com.example.domain.challenge.usecase

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetLatestDailyChallengeGateway
import jakarta.inject.Named

@Named
class GetDailyChallengeUseCase(
    private val getLatestDailyChallengeGateway: GetLatestDailyChallengeGateway,
) {
    fun execute(): Result<Challenge> {
        return getLatestDailyChallengeGateway.execute()
    }
}