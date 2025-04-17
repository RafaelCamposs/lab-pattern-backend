package com.example.application.submission.service

import com.example.application.submission.model.SubmissionModel
import com.example.application.submission.repository.SubmissionRepository
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.entity.dto.StoreSubmissionDto
import com.example.domain.submission.gateway.StoreSubmissionGateway
import org.springframework.stereotype.Component

@Component
class StoreSubmissionService (
    private val submissionRepository: SubmissionRepository
): StoreSubmissionGateway {
    override fun execute(storeSubmissionDto: StoreSubmissionDto): Result<Submission> {
        return runCatching {
            val submissionModel = SubmissionModel.fromStoreSubmissionDto(storeSubmissionDto)

            submissionRepository.save(submissionModel).toDomain()
        }
    }
}