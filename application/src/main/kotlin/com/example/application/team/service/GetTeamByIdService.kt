package com.example.application.team.service

import com.example.application.team.repository.TeamRepository
import com.example.domain.team.entity.Team
import com.example.domain.team.gateway.GetTeamByIdGateway
import org.springframework.stereotype.Component

@Component
class GetTeamByIdService(
    private val teamRepository: TeamRepository,
): GetTeamByIdGateway {
    override fun execute(id: Long): Result<Team?> {
        return runCatching {
            teamRepository.findById(id).orElseThrow {
                NoSuchElementException("Team not found")
            }.toDomain()
        }
    }
}
