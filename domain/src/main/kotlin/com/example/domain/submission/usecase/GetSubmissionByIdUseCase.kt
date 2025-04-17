package com.example.domain.submission.usecase

import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetSubmissionByIdGateway
import jakarta.inject.Named
import java.util.UUID

@Named
class GetSubmissionByIdUseCase (
    private val getSubmissionByIdGateway: GetSubmissionByIdGateway,
) {
    fun execute(id: UUID): Result<Submission> {
        return getSubmissionByIdGateway.execute(id)
    }
}