package com.example.domain.submission.usecase

import com.example.domain.challenge.gateway.GetChallengeByIdGateway
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.entity.dto.StoreSubmissionDto
import com.example.domain.submission.gateway.StoreSubmissionGateway
import com.example.domain.user.gateway.GetUserByIdGateway
import jakarta.inject.Named

@Named
class StoreSubmissionUseCase (
    private val storeSubmissionGateway: StoreSubmissionGateway,
    private val getChallengeByIdGateway: GetChallengeByIdGateway,
    private val getUserByIdGateway: GetUserByIdGateway
) {
    fun execute(storeSubmissionDto: StoreSubmissionDto): Result<Submission> {
        getChallengeByIdGateway.execute(storeSubmissionDto.challengeId).getOrThrow()
        getUserByIdGateway.execute(storeSubmissionDto.userId).getOrThrow()
        return storeSubmissionGateway.execute(storeSubmissionDto)
    }
}