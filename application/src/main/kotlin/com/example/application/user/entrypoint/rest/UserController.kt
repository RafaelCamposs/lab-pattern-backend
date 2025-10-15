package com.example.application.user.entrypoint.rest

import com.example.application.challenge.entrypoint.rest.dto.response.ChallengeResponseDto
import com.example.application.submission.entrypoint.rest.dto.response.SubmissionResponseDto
import com.example.application.user.entrypoint.rest.dto.request.UpdateUserRequestDto
import com.example.domain.user.entity.User
import com.example.application.user.entrypoint.rest.dto.UserStatisticsResponseDto
import com.example.domain.common.Page
import com.example.domain.user.usecase.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val storeUserUseCase: StoreUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getUserSolvedChallengesUseCase: GetUserSolvedChallengesUseCase,
    private val getUserSubmissionsByChallengeIdUseCase: GetUserSubmissionsByChallengeIdUseCase,
    private val getUserStatisticsUseCase: GetUserStatisticsUseCase,
) {
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody updateUserRequestDto: UpdateUserRequestDto): ResponseEntity<User> {
        val updateUserDto = updateUserRequestDto.toUpdateUserDto(id)
        return updateUserUseCase.execute(updateUserDto).fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.notFound().build() }
        )
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<Unit> {
        deleteUserUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

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