package com.example.domain.challenge.gateway

import java.util.UUID

interface CountUserSolvedChallengesGateway {
    fun execute(userId: UUID): Result<Int>
}