package com.example.domain.challenge.gateway

import com.example.domain.challenge.entity.Challenge

interface GetLatestDailyChallengeGateway {
    fun execute(): Result<Challenge>
}