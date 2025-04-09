package com.example.application.user.entrypoint.rest

import com.example.application.tournament.entrypoint.rest.dto.response.TournamentResponseDto
import com.example.application.user.entrypoint.rest.dto.request.StoreUserRequestDto
import com.example.application.user.entrypoint.rest.dto.request.UpdateUserRequestDto
import com.example.domain.tournament.entity.Tournament
import com.example.domain.user.entity.User
import com.example.domain.user.usecase.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val storeUserUseCase: StoreUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getUserTournamentsUseCase: GetUserTournamentsUseCase
) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val result = getAllUsersUseCase.execute().getOrThrow()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        return getUserByIdUseCase.execute(id).fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.notFound().build() }
        )
    }

    @PostMapping()
    fun storeUser(@RequestBody storeUserRequestDto: StoreUserRequestDto): ResponseEntity<User> {
        val storeUserDto = storeUserRequestDto.toStoreUserDto()
        val result = storeUserUseCase.execute(storeUserDto).getOrThrow()
        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updateUserRequestDto: UpdateUserRequestDto): ResponseEntity<User> {
        val updateUserDto = updateUserRequestDto.toUpdateUserDto(id)
        return updateUserUseCase.execute(updateUserDto).fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.notFound().build() }
        )
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Unit> {
        deleteUserUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}/tournaments")
    fun getUserTournaments(@PathVariable id: Long): ResponseEntity<List<TournamentResponseDto>> {
        return getUserTournamentsUseCase.execute(id).fold(
            onSuccess = { ResponseEntity.ok(it.map {
                tournament: Tournament ->  TournamentResponseDto.fromDomain(tournament)
            }) },
            onFailure = { ResponseEntity.notFound().build() }
        )
    }
}