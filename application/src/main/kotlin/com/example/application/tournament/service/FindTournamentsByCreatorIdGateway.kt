package com.example.application.tournament.service

import com.example.application.tournament.repository.TournamentRepository
import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.gateway.FindTournamentsByCreatorIdGateway
import org.springframework.stereotype.Component

@Component
class FindTournamentsByCreatorIdService (
    private val tournamentRepository: TournamentRepository,
): FindTournamentsByCreatorIdGateway {
    override fun execute(creatorId: Long): Result<List<Tournament>> {
        return runCatching {
            tournamentRepository.findByCreatorId(creatorId).map { it.toDomain() }
        }
    }
}