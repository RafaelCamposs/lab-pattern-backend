package com.example.application.submission.service

import com.example.application.submission.repository.SubmissionRepository
import com.example.application.submission.repository.projections.SubmissionWithEvaluationDto
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetAllUserSubmissionsWithEvaluationsGateway
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GetAllUserSubmissionsWithEvaluationService (
    private val submissionRepository: SubmissionRepository
): GetAllUserSubmissionsWithEvaluationsGateway {
    override fun execute(userId: UUID): Result<List<Submission>> {
        return runCatching {
            val submissionsWithEvaluation  = submissionRepository.findAllByUserWithEvaluation(userId)

            submissionsWithEvaluation.map {
                SubmissionWithEvaluationDto.toDomain(it)
            }
        }
    }
}