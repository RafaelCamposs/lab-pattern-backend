package com.example.application.team.service

import com.example.application.team.repository.TeamRepository
import com.example.domain.team.entity.Team
import com.example.domain.team.entity.dto.UpdateTeamDto
import com.example.domain.team.gateway.UpdateTeamGateway
import org.springframework.stereotype.Component

@Component
class UpdateTeamService (
    private val teamRepository: TeamRepository,
) : UpdateTeamGateway {

    override fun execute(id: Long, updateTeamDto: UpdateTeamDto): Result<Team> {
        return kotlin.runCatching {
            //TODO(ADD VALIDATIONS FOR TEAMS ENROLLED IN TOURNAMENTS)
            val teamModel = teamRepository.findById(id).orElseThrow { NoSuchElementException("Team not found") }

            val updatedTeamModel = teamModel.copy(
                name = updateTeamDto.name,
                description = updateTeamDto.description,
                imageUrl = updateTeamDto.imageUrl
            )

            teamRepository.save(updatedTeamModel).toDomain()
        }
    }
}