package com.example.domain.team.gateway

import com.example.domain.team.entity.Team
import com.example.domain.team.entity.dto.UpdateTeamDto

interface UpdateTeamGateway {
    fun execute(id: Long, updateTeamDto: UpdateTeamDto): Result<Team>
}