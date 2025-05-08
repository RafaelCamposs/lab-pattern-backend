package com.example.domain.user.usecase

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.gateway.GetUserSolvedChallengesGateway
import com.example.domain.common.Page
import jakarta.inject.Named
import java.util.UUID

@Named
class GetUserSolvedChallengesUseCase (
    private val getUserSolvedChallengesGateway: GetUserSolvedChallengesGateway
) {
    fun execute(id: UUID, page: Int, pageSize: Int) : Result<Page<Challenge>> {
        return getUserSolvedChallengesGateway.execute(
            id = id,
            page = page,
            pageSize = pageSize
        )
    }
}