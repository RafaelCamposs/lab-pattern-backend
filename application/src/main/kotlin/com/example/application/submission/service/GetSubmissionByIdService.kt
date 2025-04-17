package com.example.application.submission.service

import com.example.application.submission.repository.SubmissionRepository
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetSubmissionByIdGateway
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GetSubmissionByIdService (
    private val submissionRepository: SubmissionRepository
) : GetSubmissionByIdGateway {
    override fun execute(id: UUID): Result<Submission> {
        return runCatching {
            submissionRepository.findById(id)
                .orElseThrow { NoSuchElementException("Submission not found") }
                .toDomain()
        }
    }
}