package com.example.application.tournament.service

import com.example.application.tournament.repository.TournamentRepository
import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.gateway.GetTournamentByIdGateway
import org.springframework.stereotype.Component

@Component
class GetTournamentByIdService (
    private val tournamentRepository: TournamentRepository,
) : GetTournamentByIdGateway {
    override fun execute(tournamentId: Long): Result<Tournament> {
        return kotlin.runCatching {
            tournamentRepository.findById(tournamentId)
                .orElseThrow { NoSuchElementException("User not found") }
                .toDomain()
        }
    }
}