package com.example.domain.tournament.usecase

import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.entity.dto.UpdateTournamentDto
import com.example.domain.tournament.gateway.GetTournamentByIdGateway
import com.example.domain.tournament.gateway.UpdateTournamentGateway
import jakarta.inject.Named

@Named
class UpdateTournamentUseCase (
    private val updateTournamentGateway: UpdateTournamentGateway,
    private val getTournamentByIdGateway: GetTournamentByIdGateway
) {
    fun execute(id: Long,updateTournamentDto: UpdateTournamentDto): Result<Tournament> {
        val tournament = getTournamentByIdGateway.execute(id).getOrThrow()

        if (tournament.isUpdatable()) {
            return Result.failure(IllegalStateException("Tournament status is not updatable"))
        }

        val updatedTournament = updateTournament(tournament, updateTournamentDto)

        return updateTournamentGateway.execute(updatedTournament)
    }

    private fun updateTournament(
        tournament: Tournament,
        updateTournamentDto: UpdateTournamentDto
    ): Tournament {
        val updatedTournament = tournament.copy(
            name = updateTournamentDto.name,
            description = updateTournamentDto.description,
            startDate = updateTournamentDto.startDate,
            endDate = updateTournamentDto.endDate,
            location = updateTournamentDto.location,
            maxParticipants = updateTournamentDto.maxParticipants
        )
        return updatedTournament
    }
}