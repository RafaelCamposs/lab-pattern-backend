package com.example.domain.team.gateway

import com.example.domain.team.entity.Team
import com.example.domain.team.entity.dto.StoreTeamDto

interface StoreTeamGateway {
    fun execute(storeTeamDto: StoreTeamDto): Result<Team>
}