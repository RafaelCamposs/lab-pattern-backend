package com.example.domain.user.gateway

import java.util.*

interface DeleteUserGateway {
    fun execute(id: UUID): Result<Unit>
}