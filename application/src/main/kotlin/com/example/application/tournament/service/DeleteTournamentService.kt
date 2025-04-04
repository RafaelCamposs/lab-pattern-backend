package com.example.application.tournament.service

import com.example.application.tournament.repository.TournamentRepository
import com.example.domain.tournament.gateway.DeleteTournamentGateway
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DeleteTournamentService (
    private val tournamentRepository: TournamentRepository,
): DeleteTournamentGateway {
    override fun execute(tournamentId: Long): Result<Unit> {
        return kotlin.runCatching {
            val tournamentModel = tournamentRepository.findById(tournamentId).orElseThrow { NotFoundException() }

            tournamentRepository.save(
                tournamentModel.copy(
                    deletedAt = LocalDateTime.now()
                )
            )
        }
    }
}