package com.example.application.user.entrypoint.rest

import com.example.application.user.entrypoint.rest.dto.AuthenticationResponseDto
import com.example.application.user.entrypoint.rest.dto.LoginRequestDto
import com.example.application.user.entrypoint.rest.dto.SignUpRequestDto
import com.example.application.user.service.AuthenticationService
import com.example.domain.user.entity.dto.StoreUserDto
import com.example.domain.user.usecase.StoreUserUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val storeUserUseCase: StoreUserUseCase,
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signUpRequestDto: SignUpRequestDto): ResponseEntity<AuthenticationResponseDto> {
        val user = storeUserUseCase.execute(
            StoreUserDto(
                name = signUpRequestDto.username,
                email = signUpRequestDto.email,
                password = signUpRequestDto.password
            )
        ).getOrThrow()
        return ResponseEntity.ok(authenticationService.signup(user))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<AuthenticationResponseDto> {
        return ResponseEntity.ok(authenticationService.login(loginRequestDto))
    }
}
