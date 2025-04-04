package com.example.domain.tournament.usecase

import com.example.domain.tournament.gateway.DeleteTournamentGateway
import com.example.domain.tournament.gateway.GetTournamentByIdGateway
import jakarta.inject.Named

@Named
class DeleteTournamentUseCase (
    private val getTournamentByIdGateway: GetTournamentByIdGateway,
    private val deleteTournamentGateway: DeleteTournamentGateway
) {
    fun execute(tournamentId: Long): Result<Unit> {
        val tournament = getTournamentByIdGateway.execute(tournamentId).getOrThrow()

        if (tournament.isDeletable()) {
            return Result.failure(IllegalStateException("Tournament already deleted"))
        }

        return deleteTournamentGateway.execute(tournamentId)
    }
}