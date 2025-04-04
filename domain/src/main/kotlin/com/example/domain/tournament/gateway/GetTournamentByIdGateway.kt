package com.example.domain.tournament.gateway

import com.example.domain.tournament.entity.Tournament

interface GetTournamentByIdGateway {
    fun execute(tournamentId: Long): Result<Tournament>
}