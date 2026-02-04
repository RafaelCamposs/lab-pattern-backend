package com.example.domain.user.usecase

import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.gateway.GetEvaluationsBySubmissionIdsGateway
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetSubmissionsByChallengeAndUserIdGateway
import com.example.domain.submission.usecase.GetSubmissionByIdUseCase
import jakarta.inject.Named
import java.util.UUID

@Named
class GetUserSubmissionsByChallengeIdUseCase (
    private val getSubmissionsByChallengeAndUserIdGateway: GetSubmissionsByChallengeAndUserIdGateway,
    private val getEvaluationsBySubmissionIdsGateway: GetEvaluationsBySubmissionIdsGateway,
    private val getSubmissionByIdUseCase: GetSubmissionByIdUseCase
) {
    fun execute(challengeId: UUID, userId: UUID) : Result<List<Submission>> {
        val submissions = getSubmissionsByChallengeAndUserIdGateway.execute(
            challengeId = challengeId,
            userId = userId
        ).getOrThrow().map { getSubmissionByIdUseCase.execute(it.id!!).getOrThrow() }

        val evaluations = getEvaluationsBySubmissionIdsGateway.execute(
            submissions.map { it.id!! }
        ).getOrThrow()

        val submissionsWithEvaluation = this.associateEvaluationsToSubmissions(submissions, evaluations)

        return Result.success(submissionsWithEvaluation)
    }

    private fun associateEvaluationsToSubmissions(
        submissions: List<Submission>,
        evaluations: List<Evaluation>
    ): List<Submission> {
        val evaluationMap = evaluations.associateBy { it.submissionId }

        return submissions.map { submission ->
            val evaluation = evaluationMap[submission.id]
            submission.copy(evaluation = evaluation)
        }
    }
}