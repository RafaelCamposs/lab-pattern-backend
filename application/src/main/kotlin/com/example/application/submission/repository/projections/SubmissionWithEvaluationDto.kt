package com.example.application.submission.repository.projections

import com.example.application.evaluation.model.EvaluationModel
import com.example.application.submission.model.SubmissionModel
import com.example.domain.submission.entity.Submission

interface SubmissionWithEvaluationDto {
    val submissionModel: SubmissionModel
    val evaluationModel: EvaluationModel

    companion object {
        fun toDomain(submissionWithEvaluationDto : SubmissionWithEvaluationDto): Submission {
            return submissionWithEvaluationDto.submissionModel.toDomain().copy(
                evaluation = submissionWithEvaluationDto.evaluationModel.toDomain()
            )
        }
    }
}