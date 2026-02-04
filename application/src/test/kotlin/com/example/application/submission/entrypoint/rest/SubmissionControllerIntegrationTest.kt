package com.example.application.submission.entrypoint.rest

import com.example.application.config.BaseIntegrationTest
import com.example.application.submission.entrypoint.rest.dto.request.StoreSubmissionRequestDto
import com.example.application.user.entrypoint.rest.dto.SignUpRequestDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

class SubmissionControllerIntegrationTest : BaseIntegrationTest() {

    private lateinit var authToken: String
    private lateinit var userId: UUID
    private lateinit var challengeId: UUID
    private lateinit var patternId: UUID

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

        userId = UUID.fromString(
            jdbcTemplate.queryForObject(
                "SELECT BIN_TO_UUID(id) FROM user WHERE email = ?",
                String::class.java,
                "test@example.com"
            )
        )

        patternId = UUID.fromString(
            jdbcTemplate.queryForObject(
                "SELECT BIN_TO_UUID(id) FROM design_pattern WHERE name = ? LIMIT 1",
                String::class.java,
                "Singleton"
            )!!
        )

        jdbcTemplate.execute("""
            INSERT INTO challenge (id, title, description, expected_pattern_id, published_at, created_at, is_daily)
            SELECT
                UNHEX(REPLACE(UUID(), '-', '')) as id,
                'Test Challenge' as title,
                'Test challenge description' as description,
                id as expected_pattern_id,
                NOW() as published_at,
                NOW() as created_at,
                FALSE as is_daily
            FROM design_pattern
            WHERE name = 'Singleton'
            LIMIT 1
        """)

        challengeId = UUID.fromString(
            jdbcTemplate.queryForObject(
                "SELECT BIN_TO_UUID(id) FROM challenge WHERE title = ? LIMIT 1",
                String::class.java,
                "Test Challenge"
            )!!
        )
    }

    @Test
    fun `should create submission successfully with mocked OpenAI evaluation`() {
        val submissionRequest = StoreSubmissionRequestDto(
            userId = userId,
            challengeId = challengeId,
            patternId = patternId,
            code = "public class Singleton { private static Singleton instance; }",
            language = "java"
        )

        val result = mockMvc.perform(
            post("/v1/submissions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submissionRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.userId").value(userId.toString()))
            .andExpect(jsonPath("$.challengeId").value(challengeId.toString()))
            .andExpect(jsonPath("$.selectedPatternName").value("Singleton"))
            .andExpect(jsonPath("$.expectedPatternName").value("Singleton"))
            .andExpect(jsonPath("$.code").exists())
            .andExpect(jsonPath("$.language").value("java"))
            .andReturn()

        val submissionResponse = objectMapper.readTree(result.response.contentAsString)
        val submissionId = UUID.fromString(submissionResponse.get("id").asText())

        val submissionCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM submission WHERE id = UNHEX(REPLACE(?, '-', ''))",
            Int::class.java,
            submissionId.toString()
        )
        assert(submissionCount == 1)

        val evaluationCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM evaluation WHERE submission_id = UNHEX(REPLACE(?, '-', ''))",
            Int::class.java,
            submissionId.toString()
        )
        assert(evaluationCount == 1)
    }

    @Test
    fun `should fail to create submission without authentication`() {
        val submissionRequest = StoreSubmissionRequestDto(
            userId = userId,
            challengeId = challengeId,
            patternId = patternId,
            code = "test code",
            language = "java"
        )

        mockMvc.perform(
            post("/v1/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submissionRequest))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should get submission by id successfully`() {
        val submissionRequest = StoreSubmissionRequestDto(
            userId = userId,
            challengeId = challengeId,
            patternId = patternId,
            code = "class Example { }",
            language = "kotlin"
        )

        val createResult = mockMvc.perform(
            post("/v1/submissions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submissionRequest))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val submissionResponse = objectMapper.readTree(createResult.response.contentAsString)
        val submissionId = submissionResponse.get("id").asText()

        mockMvc.perform(
            get("/v1/submissions/$submissionId")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(submissionId))
            .andExpect(jsonPath("$.userId").value(userId.toString()))
            .andExpect(jsonPath("$.challengeId").value(challengeId.toString()))
            .andExpect(jsonPath("$.code").value("class Example { }"))
            .andExpect(jsonPath("$.language").value("kotlin"))
    }

    @Test
    fun `should return 404 when getting non-existent submission`() {
        val nonExistentId = UUID.randomUUID()

        mockMvc.perform(
            get("/v1/submissions/$nonExistentId")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should fail to get submission without authentication`() {
        val submissionId = UUID.randomUUID()

        mockMvc.perform(
            get("/v1/submissions/$submissionId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should create multiple submissions for same challenge`() {
        val firstSubmission = StoreSubmissionRequestDto(
            userId = userId,
            challengeId = challengeId,
            patternId = patternId,
            code = "first attempt",
            language = "java"
        )

        mockMvc.perform(
            post("/v1/submissions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstSubmission))
        )
            .andExpect(status().isCreated)

        val secondSubmission = StoreSubmissionRequestDto(
            userId = userId,
            challengeId = challengeId,
            patternId = patternId,
            code = "second attempt",
            language = "java"
        )

        mockMvc.perform(
            post("/v1/submissions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondSubmission))
        )
            .andExpect(status().isCreated)

        val submissionCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM submission WHERE user_id = UNHEX(REPLACE(?, '-', '')) AND challenge_id = UNHEX(REPLACE(?, '-', ''))",
            Int::class.java,
            userId.toString(),
            challengeId.toString()
        )
        assert(submissionCount == 2)
    }

    @Test
    fun `should verify evaluation contains mocked OpenAI data`() {
        val submissionRequest = StoreSubmissionRequestDto(
            userId = userId,
            challengeId = challengeId,
            patternId = patternId,
            code = "test code for evaluation",
            language = "java"
        )

        val result = mockMvc.perform(
            post("/v1/submissions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submissionRequest))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val submissionResponse = objectMapper.readTree(result.response.contentAsString)
        val submissionId = UUID.fromString(submissionResponse.get("id").asText())

        val evaluation = jdbcTemplate.queryForMap(
            "SELECT score FROM evaluation WHERE submission_id = UNHEX(REPLACE(?, '-', ''))",
            submissionId.toString()
        )

        assert(evaluation["score"] == 85)
    }
}
