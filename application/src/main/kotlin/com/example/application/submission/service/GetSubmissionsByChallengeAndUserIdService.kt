package com.example.application.submission.service

import com.example.application.submission.repository.SubmissionRepository
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetSubmissionsByChallengeAndUserIdGateway
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetSubmissionsByChallengeAndUserIdService (
    private val submissionRepository: SubmissionRepository
) : GetSubmissionsByChallengeAndUserIdGateway {
    override fun execute(challengeId: UUID, userId: UUID): Result<List<Submission>> {
        return runCatching {
            submissionRepository.findByUserIdAndChallengeId(
                challengeId = challengeId,
                userId = userId
            ).map {it.toDomain()}
        }
    }
}