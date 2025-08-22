package com.example.domain.user.usecase

import com.example.domain.user.entity.User
import com.example.domain.user.gateway.GetUserByIdGateway
import jakarta.inject.Named
import java.util.*

@Named
class GetUserByIdUseCase(
    private val getUserByIdGateway: GetUserByIdGateway
) {
    fun execute(id: UUID): Result<User> {
        return getUserByIdGateway.execute(id)
    }
}