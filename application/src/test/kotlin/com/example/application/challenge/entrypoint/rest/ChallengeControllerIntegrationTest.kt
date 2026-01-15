package com.example.application.challenge.entrypoint.rest

import com.example.application.config.BaseIntegrationTest
import com.example.application.user.entrypoint.rest.dto.SignUpRequestDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ChallengeControllerIntegrationTest : BaseIntegrationTest() {

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
    fun `should generate challenge successfully with mocked OpenAI`() {
        mockMvc.perform(
            post("/v1/challenges")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Test Challenge Title"))
            .andExpect(jsonPath("$.description").value("Test challenge description for design pattern"))
            .andExpect(jsonPath("$.isDaily").value(false))
            .andExpect(jsonPath("$.publishedAt").exists())

        val challengeCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM challenge WHERE title = ?",
            Int::class.java,
            "Test Challenge Title"
        )
        assert(challengeCount == 1)
    }

    @Test
    fun `should fail to generate challenge without authentication`() {
        mockMvc.perform(
            post("/v1/challenges")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should get daily challenge successfully`() {
        jdbcTemplate.execute("""
            INSERT INTO challenge (id, title, description, expected_pattern_id, published_at, created_at, is_daily)
            SELECT
                UNHEX(REPLACE(UUID(), '-', '')) as id,
                'Daily Challenge' as title,
                'Daily challenge description' as description,
                dp.id as expected_pattern_id,
                NOW() as published_at,
                NOW() as created_at,
                TRUE as is_daily
            FROM design_pattern dp
            WHERE dp.name = 'Singleton'
            LIMIT 1
        """)

        mockMvc.perform(
            get("/v1/challenges/daily")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Daily Challenge"))
            .andExpect(jsonPath("$.description").value("Daily challenge description"))
            .andExpect(jsonPath("$.isDaily").value(true))
            .andExpect(jsonPath("$.publishedAt").exists())
    }

    @Test
    fun `should fail to get daily challenge without authentication`() {
        mockMvc.perform(
            get("/v1/challenges/daily")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should fail when no daily challenge exists`() {
        jdbcTemplate.execute("DELETE FROM challenge WHERE is_daily = TRUE")

        mockMvc.perform(
            get("/v1/challenges/daily")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is5xxServerError)
    }

    @Test
    fun `should generate multiple challenges successfully`() {
        mockMvc.perform(
            post("/v1/challenges")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)

        mockMvc.perform(
            post("/v1/challenges")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)

        val challengeCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM challenge WHERE title = ?",
            Int::class.java,
            "Test Challenge Title"
        )
        assert(challengeCount == 2)
    }

    @Test
    fun `should get daily challenge with correct structure`() {
        jdbcTemplate.execute("""
            INSERT INTO challenge (id, title, description, expected_pattern_id, published_at, created_at, is_daily)
            SELECT
                UNHEX(REPLACE(UUID(), '-', '')) as id,
                'Structured Daily Challenge' as title,
                'Complete description' as description,
                dp.id as expected_pattern_id,
                NOW() as published_at,
                NOW() as created_at,
                TRUE as is_daily
            FROM design_pattern dp
            WHERE dp.name = 'Observer'
            LIMIT 1
        """)

        val result = mockMvc.perform(
            get("/v1/challenges/daily")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val challenge = objectMapper.readTree(result.response.contentAsString)

        assert(challenge.has("id"))
        assert(challenge.has("title"))
        assert(challenge.has("description"))
        assert(challenge.has("isDaily"))
        assert(challenge.has("publishedAt"))
        assert(challenge.get("title").asText() == "Structured Daily Challenge")

        assert(!challenge.has("expectedPatternId"))
    }
}
