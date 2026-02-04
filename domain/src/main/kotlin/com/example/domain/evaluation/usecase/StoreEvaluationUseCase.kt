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
        selectedPattern: DesignPattern,
        expectedPattern: DesignPattern,
    ): Result<Evaluation> {
        return runCatching {
            val aiEvaluation = evaluateSubmissionWithAiGateway.execute(
                selectedPattern = selectedPattern,
                expectedPattern = expectedPattern,
                challenge = challenge,
                submission = submission,
            ).getOrThrow()

            val storeEvaluationDto = StoreEvaluationDto(
                submissionId = submission.id!!,
                userId = submission.userId,
                aiEvaluationDto = aiEvaluation,
            )

            storeEvaluationGateway.execute(storeEvaluationDto).getOrThrow()
        }
    }
}