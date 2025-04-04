package com.example.domain.tournament.gateway

import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.entity.dto.StoreTournamentDto

interface StoreTournamentGateway {
    fun execute(storeTournamentDto: StoreTournamentDto): Result<Tournament>
}