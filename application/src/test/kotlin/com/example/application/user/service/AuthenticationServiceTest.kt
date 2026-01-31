package com.example.application.user.service

import com.example.application.config.jwt.JwtService
import com.example.application.user.entrypoint.rest.dto.LoginRequestDto
import com.example.domain.user.entity.User as UserEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class AuthenticationServiceTest {
    private val findUserByEmailService: FindUserByEmailService = mockk()
    private val jwtService: JwtService = mockk()
    private val authenticationManager: AuthenticationManager = mockk()

    private val service = AuthenticationService(
        findUserByEmailService,
        jwtService,
        authenticationManager
    )

    @Test
    fun `should signup user successfully`() {
        val user = UserEntity(
            id = UUID.randomUUID(),
            name = "John Doe",
            email = "john@example.com",
            password = "encodedPassword"
        )

        val jwtToken = "jwt.token.here"
        val userDetailsSlot = slot<UserDetails>()
        val extraClaimsSlot = slot<Map<String, String>>()

        every {
            jwtService.generateToken(capture(extraClaimsSlot), capture(userDetailsSlot))
        } returns jwtToken

        val result = service.signup(user)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(jwtToken, result.token)

        verify { jwtService.generateToken(any(), any()) }

        Assertions.assertEquals(user.email, userDetailsSlot.captured.username)
        Assertions.assertEquals(user.id.toString(), extraClaimsSlot.captured["userId"])
    }

    @Test
    fun `should login user successfully`() {
        val loginRequest = LoginRequestDto(
            email = "john@example.com",
            password = "plainPassword"
        )

        val user = UserEntity(
            id = UUID.randomUUID(),
            name = "John Doe",
            email = loginRequest.email,
            password = "encodedPassword"
        )

        val jwtToken = "jwt.token.here"
        val authTokenSlot = slot<UsernamePasswordAuthenticationToken>()
        val userDetailsSlot = slot<UserDetails>()
        val extraClaimsSlot = slot<Map<String, String>>()

        every { authenticationManager.authenticate(capture(authTokenSlot)) } returns mockk()
        every { findUserByEmailService.execute(loginRequest.email) } returns Result.success(user)
        every {
            jwtService.generateToken(capture(extraClaimsSlot), capture(userDetailsSlot))
        } returns jwtToken

        val result = service.login(loginRequest)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(jwtToken, result.token)

        verify { authenticationManager.authenticate(any()) }
        verify { findUserByEmailService.execute(loginRequest.email) }
        verify { jwtService.generateToken(any(), any()) }

        Assertions.assertEquals(loginRequest.email, authTokenSlot.captured.principal)
        Assertions.assertEquals(loginRequest.password, authTokenSlot.captured.credentials)
        Assertions.assertEquals(user.email, userDetailsSlot.captured.username)
        Assertions.assertEquals(user.id.toString(), extraClaimsSlot.captured["userId"])
    }

    @Test
    fun `should fail login when user not found`() {
        val loginRequest = LoginRequestDto(
            email = "notfound@example.com",
            password = "plainPassword"
        )

        every { authenticationManager.authenticate(any()) } returns mockk()
        every { findUserByEmailService.execute(loginRequest.email) } returns Result.success(null)

        val exception = Assertions.assertThrows(IllegalArgumentException::class.java) {
            service.login(loginRequest)
        }

        Assertions.assertEquals("Usuário não encontrado", exception.message)
        verify { authenticationManager.authenticate(any()) }
        verify { findUserByEmailService.execute(loginRequest.email) }
        verify(exactly = 0) { jwtService.generateToken(any(), any()) }
    }

    @Test
    fun `should fail login when findUserByEmailService fails`() {
        val loginRequest = LoginRequestDto(
            email = "john@example.com",
            password = "plainPassword"
        )

        val error = RuntimeException("Database error")

        every { authenticationManager.authenticate(any()) } returns mockk()
        every { findUserByEmailService.execute(loginRequest.email) } returns Result.failure(error)

        val exception = Assertions.assertThrows(RuntimeException::class.java) {
            service.login(loginRequest)
        }

        Assertions.assertEquals(error, exception)
        verify { authenticationManager.authenticate(any()) }
        verify { findUserByEmailService.execute(loginRequest.email) }
        verify(exactly = 0) { jwtService.generateToken(any(), any()) }
    }
}
