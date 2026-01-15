package com.example.application.user.entrypoint.rest

import com.example.application.config.BaseIntegrationTest
import com.example.application.user.entrypoint.rest.dto.LoginRequestDto
import com.example.application.user.entrypoint.rest.dto.SignUpRequestDto
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class AuthenticationControllerIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `should signup new user successfully`() {
        val signUpRequest = SignUpRequestDto(
            username = "New User",
            email = "newuser@example.com",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.token").isNotEmpty)

        val userCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM user WHERE email = ?",
            Int::class.java,
            "newuser@example.com"
        )
        assert(userCount == 1)
    }

    @Test
    fun `should fail signup with duplicate email`() {
        val signUpRequest = SignUpRequestDto(
            username = "User",
            email = "duplicate@example.com",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        )
            .andExpect(status().is4xxClientError)
    }

    @Test
    fun `should login with valid credentials`() {
        val signUpRequest = SignUpRequestDto(
            username = "Login User",
            email = "login@example.com",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        )

        val loginRequest = LoginRequestDto(
            email = "login@example.com",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.token").isNotEmpty)
    }

    @Test
    fun `should fail login with invalid credentials`() {
        val signUpRequest = SignUpRequestDto(
            username = "User",
            email = "user@example.com",
            password = "correctpassword"
        )

        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        )

        val loginRequest = LoginRequestDto(
            email = "user@example.com",
            password = "wrongpassword"
        )

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should fail login with non-existent email`() {
        val loginRequest = LoginRequestDto(
            email = "nonexistent@example.com",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should fail signup with invalid email format`() {
        val signUpRequest = SignUpRequestDto(
            username = "User",
            email = "invalid-email",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        )
            .andExpect(status().is4xxClientError)
    }
}
