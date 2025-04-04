package com.example.domain.tournament.gateway

interface DeleteTournamentGateway {
    fun execute(tournamentId: Long): Result<Unit>
}