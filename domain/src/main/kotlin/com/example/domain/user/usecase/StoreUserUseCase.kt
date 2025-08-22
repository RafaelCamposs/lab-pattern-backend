package com.example.domain.user.usecase

import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.StoreUserDto
import com.example.domain.user.gateway.FindUserByEmailGateway
import com.example.domain.user.gateway.StoreUserGateway
import jakarta.inject.Named
import java.lang.IllegalArgumentException


@Named
class StoreUserUseCase (
    private val findUserByEmailGateway: FindUserByEmailGateway,
    private val storeUserGateway: StoreUserGateway
) {
    fun execute(storeUserDto: StoreUserDto): Result<User> {
        val user = findUserByEmailGateway.execute(storeUserDto.email).getOrThrow()
        return if (user == null) {
            storeUserGateway.execute(storeUserDto)
        } else {
            Result.failure(
                IllegalArgumentException(
                    "User already Exists"
                )
            )
        }
    }
}