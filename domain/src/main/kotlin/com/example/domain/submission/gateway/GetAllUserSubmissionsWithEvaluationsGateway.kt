package com.example.domain.submission.gateway

import com.example.domain.submission.entity.Submission
import java.util.UUID

interface GetAllUserSubmissionsWithEvaluationsGateway {
    fun execute(userId: UUID): Result<List<Submission>>
}