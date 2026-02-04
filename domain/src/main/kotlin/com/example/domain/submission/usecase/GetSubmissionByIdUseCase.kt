package com.example.domain.submission.usecase

import com.example.domain.challenge.gateway.GetChallengeByIdGateway
import com.example.domain.pattern.gateway.GetPatternByIdGateway
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetSubmissionByIdGateway
import jakarta.inject.Named
import java.util.UUID

@Named
class GetSubmissionByIdUseCase (
    private val getSubmissionByIdGateway: GetSubmissionByIdGateway,
    private val getChallengeByIdGateway: GetChallengeByIdGateway,
    private val getPatternByIdGateway: GetPatternByIdGateway,
) {
    fun execute(id: UUID): Result<Submission> {
        return runCatching {
            val submission = getSubmissionByIdGateway.execute(id).getOrThrow()
            val challenge = getChallengeByIdGateway.execute(submission.challengeId).getOrThrow()
            val selectedPattern = getPatternByIdGateway.execute(submission.patternId).getOrThrow()
            val expectedPattern = getPatternByIdGateway.execute(challenge.expectedPatternId).getOrThrow()

            submission.copy(
                selectedPatternName = selectedPattern.name,
                expectedPatternName = expectedPattern.name,
            )
        }
    }
}