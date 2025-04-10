package com.example.domain.team.usecase

import com.example.domain.team.entity.Team
import com.example.domain.team.entity.dto.UpdateTeamDto
import com.example.domain.team.gateway.UpdateTeamGateway
import jakarta.inject.Named

@Named
class UpdateTeamUseCase(private val updateTeamGateway: UpdateTeamGateway) {
    fun execute(id: Long,team: UpdateTeamDto): Result<Team> {
        return updateTeamGateway.execute(id ,team)
    }
}