package com.example.domain.team.gateway

interface DeleteTeamGateway {
    fun execute(id: Long): Result<Unit>
}