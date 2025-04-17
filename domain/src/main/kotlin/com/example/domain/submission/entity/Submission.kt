package com.example.domain.submission.entity

import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.submission.entity.enum.SubmissionStatusEnum
import java.time.LocalDateTime
import java.util.*

data class Submission(
    val id: UUID? = null,
    val userId: UUID,
    val challengeId: UUID,
    val patternId: UUID,
    val code: String,
    val language: String,
    val submittedAt: LocalDateTime,
    val status: SubmissionStatusEnum = SubmissionStatusEnum.PENDING,
    val evaluation: Evaluation? = null,
)
