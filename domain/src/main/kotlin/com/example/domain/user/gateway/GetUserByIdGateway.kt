package com.example.domain.user.gateway

import com.example.domain.user.entity.User
import java.util.*

interface GetUserByIdGateway {
    fun execute(id: UUID): Result<User>
}