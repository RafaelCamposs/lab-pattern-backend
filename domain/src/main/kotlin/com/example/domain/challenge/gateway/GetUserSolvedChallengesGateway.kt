package com.example.domain.challenge.gateway

import com.example.domain.challenge.entity.Challenge
import java.util.UUID

interface GetUserSolvedChallengesGateway {
    fun execute(id: UUID): Result<List<Challenge>>
}