package com.example.domain.challenge.gateway

import com.example.domain.challenge.entity.Challenge
import com.example.domain.common.Page
import java.util.UUID

interface GetUserSolvedChallengesGateway {
    fun execute(id: UUID, page: Int, pageSize: Int): Result<Page<Challenge>>
}