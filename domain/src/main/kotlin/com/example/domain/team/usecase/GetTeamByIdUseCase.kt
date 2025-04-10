package com.example.domain.team.usecase

import com.example.domain.team.entity.Team
import com.example.domain.team.gateway.GetTeamByIdGateway
import jakarta.inject.Named

@Named
class GetTeamByIdUseCase(private val getTeamByIdGateway: GetTeamByIdGateway) {
    fun execute(id: Long): Result<Team?> {
        return getTeamByIdGateway.execute(id)
    }
}