package com.example.application.user.service

import com.example.application.user.repository.UserRepository
import com.example.domain.user.gateway.DeleteUserGateway
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import kotlin.NoSuchElementException

@Component
class DeleteUserService(
    private val userRepository: UserRepository
): DeleteUserGateway {
    override fun execute(id: UUID): Result<Unit> {
        return runCatching {
            if (!userRepository.existsById(id)) {
                throw NoSuchElementException("User not found")
            }
            val user = userRepository.findById(id).get()
            userRepository.save(
                user.copy(
                    deletedAt = LocalDateTime.now()
                )
            )
        }
    }
}