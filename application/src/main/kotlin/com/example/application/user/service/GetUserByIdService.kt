package com.example.application.user.service

import com.example.application.user.repository.UserRepository
import com.example.domain.user.entity.User
import com.example.domain.user.gateway.GetUserByIdGateway
import org.springframework.stereotype.Component
import java.util.*
import kotlin.NoSuchElementException

@Component
class GetUserByIdService(
    private val userJpaRepository: UserRepository
): GetUserByIdGateway {
    override fun execute(id: UUID): Result<User> {
        return runCatching {
            userJpaRepository.findById(id)
                .orElseThrow { NoSuchElementException("User not found") }
                .toDomain()
        }
    }
}