package com.example.domain.submission.gateway

import com.example.domain.submission.entity.Submission
import java.util.UUID

interface GetSubmissionByIdGateway {
    fun execute(id: UUID): Result<Submission>
}