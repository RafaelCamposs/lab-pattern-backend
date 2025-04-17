package com.example.domain.evaluation.usecase

import com.example.domain.challenge.entity.Challenge
import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.dto.StoreEvaluationDto
import com.example.domain.evaluation.gateway.EvaluateSubmissionWithAiGateway
import com.example.domain.evaluation.gateway.StoreEvaluationGateway
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.submission.entity.Submission
import jakarta.inject.Named

@Named
class StoreEvaluationUseCase (
    private val storeEvaluationGateway: StoreEvaluationGateway,
    private val evaluateSubmissionWithAiGateway: EvaluateSubmissionWithAiGateway,
) {
    fun execute(
        submission: Submission,
        challenge: Challenge,
        pattern: DesignPattern,
    ): Result<Evaluation> {
        val aiEvaluation = evaluateSubmissionWithAiGateway.execute(
            submission = submission,
            challenge = challenge,
            pattern = pattern,
        ).getOrThrow()

        val storeEvaluationDto = StoreEvaluationDto(
            submissionId = submission.id!!,
            patternId = submission.patternId,
            userId = submission.userId,
            aiEvaluationDto = aiEvaluation,
        )

        return storeEvaluationGateway.execute(storeEvaluationDto)
    }
}