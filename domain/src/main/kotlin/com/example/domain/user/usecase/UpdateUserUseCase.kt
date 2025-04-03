package com.example.domain.user.usecase

import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.UpdateUserDto
import com.example.domain.user.gateway.UpdateUserGateway
import jakarta.inject.Named

@Named
class UpdateUserUseCase(
    private val updateUserGateway: UpdateUserGateway
) {
    fun execute(updateUserDto: UpdateUserDto): Result<User> {
        return updateUserGateway.execute(updateUserDto)
    }
}