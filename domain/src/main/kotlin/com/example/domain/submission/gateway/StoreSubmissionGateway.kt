package com.example.domain.submission.gateway

import com.example.domain.submission.entity.Submission
import com.example.domain.submission.entity.dto.StoreSubmissionDto

interface StoreSubmissionGateway {
    fun execute(storeSubmissionDto: StoreSubmissionDto): Result<Submission>
}