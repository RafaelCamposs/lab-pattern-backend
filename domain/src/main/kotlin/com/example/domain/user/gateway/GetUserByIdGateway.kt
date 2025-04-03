package com.example.domain.user.gateway

import com.example.domain.user.entity.User

interface GetUserByIdGateway {
    fun execute(id: Long): Result<User>
}