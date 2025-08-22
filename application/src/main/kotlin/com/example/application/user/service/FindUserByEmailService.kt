package com.example.application.user.service

import com.example.application.user.repository.UserRepository
import com.example.domain.user.entity.User
import com.example.domain.user.gateway.FindUserByEmailGateway
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class FindUserByEmailService (
    private val userRepository: UserRepository
): FindUserByEmailGateway {
    override fun execute(email: String): Result<User?> {
        return runCatching {
            userRepository.findByEmail(email).getOrNull()?.toDomain()
        }
    }
}