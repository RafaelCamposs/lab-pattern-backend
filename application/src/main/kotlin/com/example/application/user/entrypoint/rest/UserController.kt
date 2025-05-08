package com.example.application.user.entrypoint.rest

import com.example.application.challenge.entrypoint.rest.dto.response.ChallengeResponseDto
import com.example.application.submission.entrypoint.rest.dto.response.SubmissionResponseDto
import com.example.application.user.entrypoint.rest.dto.UserStatisticsResponseDto
import com.example.domain.common.Page
import com.example.domain.user.usecase.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(allowedHeaders = ["*"], origins = ["*"])
@RestController
@RequestMapping("/v1/users")
class UserController(
    private val getUserSolvedChallengesUseCase: GetUserSolvedChallengesUseCase,
    private val getUserSubmissionsByChallengeIdUseCase: GetUserSubmissionsByChallengeIdUseCase,
    private val getUserStatisticsUseCase: GetUserStatisticsUseCase,
) {
    @GetMapping("/{id}/challenges")
    fun getUserAnsweredChallenges(
        @PathVariable id: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<Page<ChallengeResponseDto>>{
        val challenges = getUserSolvedChallengesUseCase.execute(
            id,
            page,
            pageSize
        )
            .getOrThrow()

        return ResponseEntity(
            ChallengeResponseDto.fromPageDomain(challenges),
            HttpStatus.OK
        )
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

    @GetMapping("{userId}/statistics")
    fun getUserStatistics(
        @PathVariable userId: UUID
    ): ResponseEntity<UserStatisticsResponseDto> {
        val statistics = getUserStatisticsUseCase.execute(userId).getOrThrow()

        return ResponseEntity(
            UserStatisticsResponseDto.fromDomain(statistics),
            HttpStatus.OK
        )
    }
}