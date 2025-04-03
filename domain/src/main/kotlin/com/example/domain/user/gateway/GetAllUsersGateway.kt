package com.example.domain.user.gateway

import com.example.domain.user.entity.User

interface GetAllUsersGateway {
    fun execute(): Result<List<User>>
}