package com.example.application.tournament.service

import com.example.application.tournament.model.TournamentModel
import com.example.application.tournament.repository.TournamentRepository
import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.entity.dto.StoreTournamentDto
import com.example.domain.tournament.gateway.StoreTournamentGateway
import org.springframework.stereotype.Component

@Component
class StoreTournamentService (
    private val tournamentRepository: TournamentRepository,
) : StoreTournamentGateway {
    override fun execute(storeTournamentDto: StoreTournamentDto): Result<Tournament> {
        return kotlin.runCatching {
            val tournament = TournamentModel.fromStoreTournamentDto(
                storeTournamentDto = storeTournamentDto
            )

            tournamentRepository.save(
                tournament
            ).toDomain()
        }
    }
}