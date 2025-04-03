package com.example.domain.user.gateway

import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.UpdateUserDto

interface UpdateUserGateway {
    fun execute(updateUserDto: UpdateUserDto): Result<User>
}