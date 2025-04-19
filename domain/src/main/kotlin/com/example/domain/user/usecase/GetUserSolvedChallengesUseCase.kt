package com.example.domain.user.usecase

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetUserSolvedChallengesGateway
import jakarta.inject.Named
import java.util.UUID

@Named
class GetUserSolvedChallengesUseCase (
    private val getUserSolvedChallengesGateway: GetUserSolvedChallengesGateway
) {
    fun execute(id: UUID) : Result<List<Challenge>> {
        return getUserSolvedChallengesGateway.execute(id)
    }
}