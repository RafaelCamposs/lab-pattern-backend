package com.example.application.user.service

import com.example.application.config.jwt.JwtService
import com.example.application.user.entrypoint.rest.dto.AuthenticationResponseDto
import com.example.application.user.entrypoint.rest.dto.LoginRequestDto
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import com.example.domain.user.entity.User as UserEntity
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val findUserByEmailService: FindUserByEmailService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    fun signup(user: UserEntity): AuthenticationResponseDto {
        val userDetails = User.builder()
            .username(user.email)
            .password(user.password)
            .authorities(SimpleGrantedAuthority("USER"))
            .build()
        val extraClaims = mapOf("userId" to user.id.toString())
        val jwtToken = jwtService.generateToken(extraClaims, userDetails)
        return AuthenticationResponseDto(token = jwtToken)
    }

    fun login(request: LoginRequestDto): AuthenticationResponseDto {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )
        val user = findUserByEmailService.execute(request.email).getOrThrow()
            ?: throw IllegalArgumentException("Usuário não encontrado")
        val userDetails = User.builder()
            .username(user.email)
            .password(user.password)
            .authorities(SimpleGrantedAuthority("USER"))
            .build()
        val extraClaims = mapOf("userId" to user.id.toString())
        val jwtToken = jwtService.generateToken(extraClaims, userDetails)
        return AuthenticationResponseDto(token = jwtToken)
    }
}
