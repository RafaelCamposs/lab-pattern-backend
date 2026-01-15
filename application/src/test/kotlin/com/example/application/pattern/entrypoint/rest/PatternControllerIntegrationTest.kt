package com.example.application.pattern.entrypoint.rest

import com.example.application.config.BaseIntegrationTest
import com.example.application.user.entrypoint.rest.dto.SignUpRequestDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class PatternControllerIntegrationTest : BaseIntegrationTest() {

    private lateinit var authToken: String

    @BeforeEach
    fun setup() {
        val signUpRequest = SignUpRequestDto(
            username = "Test User",
            email = "test@example.com",
            password = "password123"
        )

        val result = mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readTree(result.response.contentAsString)
        authToken = response.get("token").asText()
    }

    @Test
    fun `should list all design patterns successfully`() {
        mockMvc.perform(
            get("/v1/patterns")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(23))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].name").exists())
            .andExpect(jsonPath("$[0].category").exists())
            .andExpect(jsonPath("$[0].description").exists())
    }

    @Test
    fun `should fail to list patterns without authentication`() {
        mockMvc.perform(
            get("/v1/patterns")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should fail to list patterns with invalid token`() {
        mockMvc.perform(
            get("/v1/patterns")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return patterns with correct categories`() {
        val result = mockMvc.perform(
            get("/v1/patterns")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val patterns = objectMapper.readTree(result.response.contentAsString)

        val categories = patterns.map { it.get("category").asText() }.toSet()
        assert(categories.contains("Creational"))
        assert(categories.contains("Structural"))
        assert(categories.contains("Behavioral"))
    }

    @Test
    fun `should verify specific patterns exist in the response`() {
        val result = mockMvc.perform(
            get("/v1/patterns")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val patterns = objectMapper.readTree(result.response.contentAsString)
        val patternNames = patterns.map { it.get("name").asText() }

        assert(patternNames.contains("Singleton"))
        assert(patternNames.contains("Factory Method"))
        assert(patternNames.contains("Observer"))
        assert(patternNames.contains("Strategy"))
    }
}
