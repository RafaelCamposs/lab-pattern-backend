package com.example.application.team.service

import com.example.application.team.repository.TeamRepository
import com.example.domain.team.gateway.DeleteTeamGateway
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DeleteTeamService (
    private val teamRepository: TeamRepository,
) : DeleteTeamGateway {
    override fun execute(id: Long): Result<Unit> {
        return kotlin.runCatching {
            val teamModel = teamRepository.findById(id).orElseThrow { NoSuchElementException("Team not found") }

            teamRepository.save(
                teamModel.copy(
                    deletedAt = LocalDateTime.now()
                )
            )
        }
    }
}