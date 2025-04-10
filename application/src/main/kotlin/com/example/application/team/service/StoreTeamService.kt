package com.example.application.team.service

import com.example.application.team.model.TeamModel
import com.example.application.team.repository.TeamRepository
import com.example.domain.team.entity.Team
import com.example.domain.team.entity.dto.StoreTeamDto
import com.example.domain.team.gateway.StoreTeamGateway
import org.springframework.stereotype.Component

@Component
class StoreTeamService (
    private val teamRepository: TeamRepository,
) : StoreTeamGateway {
    override fun execute(storeTeamDto: StoreTeamDto): Result<Team> {
        return kotlin.runCatching {
            val teamModel = TeamModel.fromStoreTeamDto(storeTeamDto)

            teamRepository.save(teamModel).toDomain()
        }
    }
}