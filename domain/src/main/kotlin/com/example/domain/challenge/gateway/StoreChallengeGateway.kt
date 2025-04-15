package com.example.domain.challenge.gateway

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.entity.dto.StoreChallengeDto

interface StoreChallengeGateway {
    fun execute(storeChallengeDto: StoreChallengeDto): Result<Challenge>
}