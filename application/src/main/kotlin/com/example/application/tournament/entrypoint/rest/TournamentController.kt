package com.example.application.tournament.entrypoint.rest

import com.example.application.tournament.entrypoint.rest.dto.request.StoreTournamentRequestDto
import com.example.application.tournament.entrypoint.rest.dto.request.UpdateTournamentRequestDto
import com.example.application.tournament.entrypoint.rest.dto.response.TournamentResponseDto
import com.example.domain.tournament.usecase.DeleteTournamentUseCase
import com.example.domain.tournament.usecase.GetTournamentByIdUseCase
import com.example.domain.tournament.usecase.StoreTournamentUseCase
import com.example.domain.tournament.usecase.UpdateTournamentUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/tournaments")
class TournamentController (
    private val storeTournamentUseCase: StoreTournamentUseCase,
    private val getTournamentByIdUseCase: GetTournamentByIdUseCase,
    private val updateTournamentUseCase: UpdateTournamentUseCase,
    private val deleteTournamentUseCase: DeleteTournamentUseCase
) {

    @PostMapping()
    fun storeTournament(
        @RequestBody storeTournamentRequestDto: StoreTournamentRequestDto
    ): ResponseEntity<TournamentResponseDto> {
        val tournament = storeTournamentUseCase.execute(storeTournamentRequestDto.toDomain()).getOrThrow()

        return ResponseEntity.ok(TournamentResponseDto.fromDomain(tournament))
    }

    @GetMapping("/{id}")
    fun getTournamentById(
        @PathVariable id: Long
    ): ResponseEntity<TournamentResponseDto> {
        return getTournamentByIdUseCase.execute(id).fold(
            onSuccess = { ResponseEntity.ok(TournamentResponseDto.fromDomain(it)) },
            onFailure = { ResponseEntity.notFound().build() }
        )
    }

    @PutMapping("/{id}")
    fun updateTournament(
        @PathVariable id: Long,
        @RequestBody updateTournamentRequestDto: UpdateTournamentRequestDto
    ): ResponseEntity<TournamentResponseDto> {
        val tournament = updateTournamentUseCase.execute(id,updateTournamentRequestDto.toDomain()).getOrThrow()

        return ResponseEntity.ok(TournamentResponseDto.fromDomain(tournament))
    }

    @DeleteMapping("/{id}")
    fun deleteTournament(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        deleteTournamentUseCase.execute(id).getOrThrow()

        return ResponseEntity.noContent().build()
    }
}