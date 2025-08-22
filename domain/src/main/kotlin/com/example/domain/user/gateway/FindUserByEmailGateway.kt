package com.example.domain.user.gateway

import com.example.domain.user.entity.User

interface FindUserByEmailGateway {
    fun execute(email: String): Result<User?>
}