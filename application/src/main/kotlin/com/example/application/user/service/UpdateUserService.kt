package com.example.application.user.service

import com.example.application.user.repository.UserRepository
import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.UpdateUserDto
import com.example.domain.user.gateway.UpdateUserGateway
import org.springframework.stereotype.Component

@Component
class UpdateUserService(
    private val userJpaRepository: UserRepository
): UpdateUserGateway {
    override fun execute(updateUserDto: UpdateUserDto): Result<User> {
        return runCatching {
            val existingUser = userJpaRepository.findById(updateUserDto.id)
                .orElseThrow { NoSuchElementException("User not found") }
            
            val updatedUser = existingUser.copy(
                name = updateUserDto.name,
                email = updateUserDto.email,
                password = updateUserDto.password
            )
            
            userJpaRepository.save(updatedUser).toDomain()
        }
    }
}