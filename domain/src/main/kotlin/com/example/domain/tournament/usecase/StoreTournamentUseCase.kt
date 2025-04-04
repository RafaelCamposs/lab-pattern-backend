package com.example.domain.tournament.usecase

import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.entity.dto.StoreTournamentDto
import com.example.domain.tournament.gateway.StoreTournamentGateway
import jakarta.inject.Named

@Named
class StoreTournamentUseCase (
    private val storeTournamentGateway: StoreTournamentGateway
) {
    fun execute(storeTournamentDto: StoreTournamentDto): Result<Tournament> {
        return storeTournamentGateway.execute(storeTournamentDto)
    }
}