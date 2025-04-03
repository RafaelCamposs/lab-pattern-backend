package com.example.domain.user.usecase

import com.example.domain.user.gateway.DeleteUserGateway
import jakarta.inject.Named

@Named
class DeleteUserUseCase(
    private val deleteUserGateway: DeleteUserGateway
) {
    fun execute(id: Long): Result<Unit> {
        return deleteUserGateway.execute(id)
    }
}