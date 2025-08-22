package com.example.domain.user.usecase

import com.example.domain.user.gateway.DeleteUserGateway
import jakarta.inject.Named
import java.util.*

@Named
class DeleteUserUseCase(
    private val deleteUserGateway: DeleteUserGateway
) {
    fun execute(id: UUID): Result<Unit> {
        return deleteUserGateway.execute(id)
    }
}