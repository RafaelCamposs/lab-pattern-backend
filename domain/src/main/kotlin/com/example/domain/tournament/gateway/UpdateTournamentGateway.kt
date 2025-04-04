package com.example.domain.tournament.gateway

import com.example.domain.tournament.entity.Tournament

interface UpdateTournamentGateway {
    fun execute(updatedTournament: Tournament): Result<Tournament>
}