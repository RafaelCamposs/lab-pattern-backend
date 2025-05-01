package com.example.application.user.entrypoint.rest

import com.example.application.challenge.entrypoint.rest.dto.response.ChallengeResponseDto
import com.example.application.submission.entrypoint.rest.dto.response.SubmissionResponseDto
import com.example.domain.user.usecase.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val getUserSolvedChallengesUseCase: GetUserSolvedChallengesUseCase,
    private val getUserSubmissionsByChallengeIdUseCase: GetUserSubmissionsByChallengeIdUseCase
) {
    @GetMapping("/{id}/challenges")
    fun getUserAnsweredChallenges(
        @PathVariable id: UUID
    ): ResponseEntity<List<ChallengeResponseDto>>{
        val challenges = getUserSolvedChallengesUseCase.execute(id)
            .getOrThrow()

        return ResponseEntity(challenges.map {
            ChallengeResponseDto.fromDomain(it)
        }, HttpStatus.OK)
    }

    @GetMapping("{userId}/challenges/{challengeId}/submissions")
    fun getUserSubmissionsForChallenge(
        @PathVariable userId: UUID,
        @PathVariable challengeId: UUID
    ): ResponseEntity<List<SubmissionResponseDto>> {
        val submissions = getUserSubmissionsByChallengeIdUseCase.execute(
            challengeId = challengeId,
            userId = userId
        ).getOrThrow()

        return ResponseEntity(
            submissions.map {
                SubmissionResponseDto.fromDomain(it)
            },
            HttpStatus.OK
        )
    }
}