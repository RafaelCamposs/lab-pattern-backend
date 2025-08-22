package com.example.domain.submission.usecase

import com.example.domain.challenge.gateway.GetChallengeByIdGateway
import com.example.domain.evaluation.usecase.StoreEvaluationUseCase
import com.example.domain.pattern.gateway.GetPatternByIdGateway
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.entity.dto.StoreSubmissionDto
import com.example.domain.submission.gateway.StoreSubmissionGateway
import com.example.domain.user.gateway.GetUserByIdGateway
import jakarta.inject.Named

@Named
class StoreSubmissionUseCase (
    private val storeSubmissionGateway: StoreSubmissionGateway,
    private val getChallengeByIdGateway: GetChallengeByIdGateway,
    private val getUserByIdGateway: GetUserByIdGateway,
    private val getPatternByIdGateway: GetPatternByIdGateway,
    private val storeEvaluationUseCase: StoreEvaluationUseCase,
) {
    fun execute(storeSubmissionDto: StoreSubmissionDto): Result<Submission> {
        return runCatching {
            val challenge = getChallengeByIdGateway.execute(storeSubmissionDto.challengeId).getOrThrow()
            val user = getUserByIdGateway.execute(storeSubmissionDto.userId).getOrThrow()
            val pattern = getPatternByIdGateway.execute(storeSubmissionDto.patternId).getOrThrow()
            val submission = storeSubmissionGateway.execute(storeSubmissionDto).getOrThrow()
            val evaluation = storeEvaluationUseCase.execute(
                submission = submission,
                challenge = challenge,
                pattern = pattern,
            ).getOrThrow()

            submission.copy(
                evaluation = evaluation,
            )
        }
    }
}