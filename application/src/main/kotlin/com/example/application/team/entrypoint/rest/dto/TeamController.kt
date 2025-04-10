package com.example.application.team.entrypoint.rest.dto

import com.example.application.team.entrypoint.rest.dto.request.StoreTeamRequestDto
import com.example.application.team.entrypoint.rest.dto.request.UpdateTeamRequestDto
import com.example.application.team.entrypoint.rest.dto.response.TeamResponseDto
import com.example.domain.team.usecase.StoreTeamUseCase
import com.example.domain.team.usecase.DeleteTeamUseCase
import com.example.domain.team.usecase.GetTeamByIdUseCase
import com.example.domain.team.usecase.UpdateTeamUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/teams")
class TeamController(
    private val storeTeamUseCase: StoreTeamUseCase,
    private val deleteTeamUseCase: DeleteTeamUseCase,
    private val getTeamByIdUseCase: GetTeamByIdUseCase,
    private val updateTeamUseCase: UpdateTeamUseCase
) {
    @PostMapping
    fun storeTeam(@RequestBody storeTeamRequestDto: StoreTeamRequestDto): ResponseEntity<TeamResponseDto> {
        val team = storeTeamRequestDto.toDomain()
        return storeTeamUseCase.execute(team).fold(
            onSuccess = { ResponseEntity(TeamResponseDto.fromDomain(it), HttpStatus.CREATED) },
            onFailure = { ResponseEntity.badRequest().build() }
        )
    }

    @DeleteMapping("/{id}")
    fun deleteTeam(@PathVariable id: Long): ResponseEntity<Unit> {
        return deleteTeamUseCase.execute(id).fold(
            onSuccess = { ResponseEntity.noContent().build() },
            onFailure = { ResponseEntity.notFound().build() }
        )
    }

    @GetMapping("/{id}")
    fun getTeam(@PathVariable id: Long): ResponseEntity<TeamResponseDto> {
        return getTeamByIdUseCase.execute(id).fold(
            onSuccess = { team ->
                if (team != null) {
                    ResponseEntity.ok(TeamResponseDto.fromDomain(team))
                } else {
                    ResponseEntity.notFound().build()
                }
            },
            onFailure = { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() }
        )
    }

    @PutMapping("/{id}")
    fun updateTeam(
        @PathVariable id: Long,
        @RequestBody updateTeamRequestDto: UpdateTeamRequestDto
    ): ResponseEntity<TeamResponseDto> {
        val team = updateTeamRequestDto.toDomain()
        return updateTeamUseCase.execute(id ,team).fold(
            onSuccess = { ResponseEntity.ok(TeamResponseDto.fromDomain(it)) },
            onFailure = { ResponseEntity.badRequest().build() }
        )
    }
}