package com.example.domain.user.usecase

import com.example.domain.user.entity.User
import com.example.domain.user.gateway.GetAllUsersGateway
import jakarta.inject.Named

@Named
class GetAllUsersUseCase(
    private val getAllUsersGateway: GetAllUsersGateway
) {
    fun execute(): Result<List<User>> {
        return getAllUsersGateway.execute()
    }
}