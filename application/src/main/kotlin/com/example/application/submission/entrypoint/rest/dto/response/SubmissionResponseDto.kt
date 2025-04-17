package com.example.application.submission.entrypoint.rest.dto.response

import com.example.domain.submission.entity.Submission
import com.example.domain.submission.entity.enum.SubmissionStatusEnum
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
    val status: SubmissionStatusEnum,
) {
    companion object {
        fun fromDomain(submission: Submission) = SubmissionResponseDto(
            id = submission.id!!,
            userId = submission.userId,
            challengeId = submission.challengeId,
            code = submission.code,
            language = submission.language,
            submittedAt = submission.submittedAt,
            status = submission.status,
            patternId = submission.patternId,
        )
    }
}
