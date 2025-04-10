package com.example.domain.team.usecase

import com.example.domain.team.gateway.DeleteTeamGateway
import jakarta.inject.Named

@Named
class DeleteTeamUseCase(private val deleteTeamGateway: DeleteTeamGateway) {
    fun execute(id: Long): Result<Unit> {
        //TODO("ADD VALIDATIONS FOR TEAMS ENROLLED IN TOURNAMENTS")
        return deleteTeamGateway.execute(id)
    }
}