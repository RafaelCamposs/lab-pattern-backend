package com.example.domain.tournament.usecase

import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.gateway.GetTournamentByIdGateway
import jakarta.inject.Named

@Named
class GetTournamentByIdUseCase (
    private val getTournamentByIdGateway: GetTournamentByIdGateway
) {
    fun execute(tournamentId: Long): Result<Tournament> {
        return getTournamentByIdGateway.execute(tournamentId)
    }
}