package com.example.domain.user.usecase

import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.StoreUserDto
import com.example.domain.user.gateway.StoreUserGateway
import jakarta.inject.Named


@Named
class StoreUserUseCase (
    private val storeUserGateway: StoreUserGateway
) {
    fun execute(storeUserDto: StoreUserDto): Result<User> {
        return storeUserGateway.execute(storeUserDto)
    }
}