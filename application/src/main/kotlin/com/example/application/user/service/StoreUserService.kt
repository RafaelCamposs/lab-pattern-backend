package com.example.application.user.service

import com.example.application.user.model.UserModel
import com.example.application.user.repository.UserRepository
import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.StoreUserDto
import com.example.domain.user.gateway.StoreUserGateway
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class StoreUserService (
    private val userJpaRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): StoreUserGateway {
    override fun execute(storeUserDto: StoreUserDto): Result<User> {
        return runCatching {
            val userModel = UserModel(
                name = storeUserDto.name,
                email = storeUserDto.email,
                password = passwordEncoder.encode(storeUserDto.password)
            )

            val user = userJpaRepository.save(userModel)

            user.toDomain()
        }
    }
}
