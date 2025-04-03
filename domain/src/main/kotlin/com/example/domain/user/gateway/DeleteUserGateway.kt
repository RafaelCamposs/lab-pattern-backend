package com.example.domain.user.gateway

interface DeleteUserGateway {
    fun execute(id: Long): Result<Unit>
}