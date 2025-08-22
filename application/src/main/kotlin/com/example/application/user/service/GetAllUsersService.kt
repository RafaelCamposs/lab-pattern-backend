package com.example.application.user.service

import com.example.application.user.repository.UserRepository
import com.example.domain.user.entity.User
import com.example.domain.user.gateway.*
import org.springframework.stereotype.Component

@Component
class GetAllUsersService(
    private val userJpaRepository: UserRepository
): GetAllUsersGateway {
    override fun execute(): Result<List<User>> {
        return runCatching {
            userJpaRepository.findAll().map { it.toDomain() }
        }
    }
}