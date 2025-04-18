package com.example.application.submission.entrypoint.rest.dto.response

import com.example.application.evaluation.entrypoint.rest.dto.response.EvaluationResponseDto
import com.example.domain.submission.entity.Submission
import java.time.LocalDateTime
import java.util.*

data class SubmissionResponseDto(
    val id: UUID,
    val userId: UUID,
    val challengeId: UUID,
    val patternId: UUID,
    val code: String,
    val language: String,
    val submittedAt: LocalDateTime,
    val evaluation: EvaluationResponseDto
) {
    companion object {
        fun fromDomain(submission: Submission) = SubmissionResponseDto(
            id = submission.id!!,
            userId = submission.userId,
            challengeId = submission.challengeId,
            code = submission.code,
            language = submission.language,
            submittedAt = submission.submittedAt,
            patternId = submission.patternId,
            evaluation = EvaluationResponseDto.fromDomain(submission.evaluation!!)
        )
    }
}
