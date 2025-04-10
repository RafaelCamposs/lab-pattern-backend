package com.example.domain.team.gateway

import com.example.domain.team.entity.Team

interface GetTeamByIdGateway {
    fun execute(id: Long): Result<Team?>
}