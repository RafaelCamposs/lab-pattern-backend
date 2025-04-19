package com.example.domain.submission.gateway

import com.example.domain.submission.entity.Submission
import java.util.UUID

interface GetSubmissionsByChallengeAndUserIdGateway {
    fun execute(challengeId: UUID, userId: UUID) : Result<List<Submission>>
}