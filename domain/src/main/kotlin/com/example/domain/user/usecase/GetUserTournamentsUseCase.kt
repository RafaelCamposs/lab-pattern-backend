package com.example.domain.user.usecase

import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.gateway.FindTournamentsByCreatorIdGateway
import jakarta.inject.Named

@Named
class GetUserTournamentsUseCase(
    private val findTournamentsByCreatorIdGateway: FindTournamentsByCreatorIdGateway,
) {
    fun execute(id: Long): Result<List<Tournament>> {
        return findTournamentsByCreatorIdGateway.execute(id)
    }
}