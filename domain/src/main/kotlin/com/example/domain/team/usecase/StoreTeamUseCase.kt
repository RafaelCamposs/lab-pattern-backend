package com.example.domain.team.usecase

import com.example.domain.team.entity.Team
import com.example.domain.team.entity.dto.StoreTeamDto
import com.example.domain.team.gateway.StoreTeamGateway
import jakarta.inject.Named

@Named
class StoreTeamUseCase(private val storeTeamGateway: StoreTeamGateway) {
    fun execute(storeTeamDto: StoreTeamDto): Result<Team> {
        return storeTeamGateway.execute(storeTeamDto)
    }
}