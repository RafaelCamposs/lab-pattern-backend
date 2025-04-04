package com.example.application.tournament.service

import com.example.application.tournament.model.TournamentModel
import com.example.application.tournament.repository.TournamentRepository
import com.example.domain.tournament.entity.Tournament
import com.example.domain.tournament.gateway.UpdateTournamentGateway
import org.springframework.stereotype.Component

@Component
class UpdateTournamentService (
    private val tournamentRepository: TournamentRepository,
) : UpdateTournamentGateway {
    override fun execute(updatedTournament: Tournament): Result<Tournament> {
        return kotlin.runCatching {

            val tournamentModel = TournamentModel.fromDomain(updatedTournament)

            tournamentRepository.save(
                tournamentModel
            ).toDomain()
        }
    }
}