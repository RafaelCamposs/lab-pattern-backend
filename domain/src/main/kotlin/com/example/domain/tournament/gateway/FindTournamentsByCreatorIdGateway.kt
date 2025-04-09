package com.example.domain.tournament.gateway

import com.example.domain.tournament.entity.Tournament

interface FindTournamentsByCreatorIdGateway {
    fun execute(creatorId: Long): Result<List<Tournament>>
}