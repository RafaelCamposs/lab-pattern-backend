package com.example.application.user.entrypoint.rest

import com.example.application.config.BaseIntegrationTest
import com.example.application.user.entrypoint.rest.dto.SignUpRequestDto
import com.example.application.user.entrypoint.rest.dto.request.UpdateUserRequestDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

class UserControllerIntegrationTest : BaseIntegrationTest() {

    private lateinit var authToken: String
    private lateinit var userId: UUID
    private lateinit var challengeId: UUID
    private lateinit var patternId: UUID
    private lateinit var submissionId: UUID

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
                'User Test Challenge' as title,
                'Description' as description,
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
                "User Test Challenge"
            )!!
        )
    }

    @Test
    fun `should update user successfully`() {
        val updateRequest = UpdateUserRequestDto(
            name = "Updated Name",
            email = "updated@example.com",
            password = "newpassword123"
        )

        mockMvc.perform(
            put("/v1/users/$userId")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.name").value("Updated Name"))
            .andExpect(jsonPath("$.email").value("updated@example.com"))

        val updatedName = jdbcTemplate.queryForObject(
            "SELECT name FROM user WHERE id = UNHEX(REPLACE(?, '-', ''))",
            String::class.java,
            userId.toString()
        )
        assert(updatedName == "Updated Name")
    }

    @Test
    fun `should return 404 when updating non-existent user`() {
        val nonExistentId = UUID.randomUUID()
        val updateRequest = UpdateUserRequestDto(
            name = "Name",
            email = "email@example.com",
            password = "password"
        )

        mockMvc.perform(
            put("/v1/users/$nonExistentId")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete user successfully with soft delete`() {
        mockMvc.perform(
            delete("/v1/users/$userId")
                .header("Authorization", "Bearer $authToken")
        )
            .andExpect(status().isNoContent)

        val deletedAt = jdbcTemplate.queryForObject(
            "SELECT deleted_at FROM user WHERE id = UNHEX(REPLACE(?, '-', ''))",
            String::class.java,
            userId.toString()
        )
        assert(deletedAt != null)
    }

    @Test
    fun `should get user solved challenges with pagination`() {
        jdbcTemplate.execute("""
            INSERT INTO submission (id, user_id, challenge_id, code, language, pattern_id, submitted_at)
            VALUES (
                UNHEX(REPLACE(UUID(), '-', '')),
                UNHEX(REPLACE('$userId', '-', '')),
                UNHEX(REPLACE('$challengeId', '-', '')),
                'test code',
                'java',
                UNHEX(REPLACE('$patternId', '-', '')),
                NOW()
            )
        """)

        mockMvc.perform(
            get("/v1/users/$userId/challenges")
                .header("Authorization", "Bearer $authToken")
                .param("page", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.currentPage").value(0))
            .andExpect(jsonPath("$.pageSize").value(10))
            .andExpect(jsonPath("$.totalElements").exists())
    }

    @Test
    fun `should get user submissions for specific challenge`() {
        jdbcTemplate.execute("""
            INSERT INTO submission (id, user_id, challenge_id, code, language, pattern_id, submitted_at)
            VALUES (
                UNHEX(REPLACE(UUID(), '-', '')),
                UNHEX(REPLACE('$userId', '-', '')),
                UNHEX(REPLACE('$challengeId', '-', '')),
                'first attempt',
                'java',
                UNHEX(REPLACE('$patternId', '-', '')),
                NOW()
            )
        """)

        jdbcTemplate.execute("""
            INSERT INTO submission (id, user_id, challenge_id, code, language, pattern_id, submitted_at)
            VALUES (
                UNHEX(REPLACE(UUID(), '-', '')),
                UNHEX(REPLACE('$userId', '-', '')),
                UNHEX(REPLACE('$challengeId', '-', '')),
                'second attempt',
                'java',
                UNHEX(REPLACE('$patternId', '-', '')),
                NOW()
            )
        """)

        mockMvc.perform(
            get("/v1/users/$userId/challenges/$challengeId/submissions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    fun `should get user statistics successfully`() {
        jdbcTemplate.execute("""
            INSERT INTO submission (id, user_id, challenge_id, code, language, pattern_id, submitted_at)
            VALUES (
                UNHEX(REPLACE(UUID(), '-', '')),
                UNHEX(REPLACE('$userId', '-', '')),
                UNHEX(REPLACE('$challengeId', '-', '')),
                'test code',
                'java',
                UNHEX(REPLACE('$patternId', '-', '')),
                NOW()
            )
        """)

        val submissionIdStr = jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id) FROM submission WHERE user_id = UNHEX(REPLACE(?, '-', '')) LIMIT 1",
            String::class.java,
            userId.toString()
        )!!

        jdbcTemplate.execute("""
            INSERT INTO evaluation (id, submission_id, score, feedback, evaluated_at)
            VALUES (
                UNHEX(REPLACE(UUID(), '-', '')),
                UNHEX(REPLACE('$submissionIdStr', '-', '')),
                90,
                'Great job',
                NOW()
            )
        """)

        jdbcTemplate.execute("""
            INSERT INTO user_progress (user_id, total_score, challenges_completed, last_submission_at)
            VALUES (
                UNHEX(REPLACE('$userId', '-', '')),
                90,
                1,
                NOW()
            )
            ON DUPLICATE KEY UPDATE
                total_score = 90,
                challenges_completed = 1,
                last_submission_at = NOW()
        """)

        mockMvc.perform(
            get("/v1/users/$userId/statistics")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.completedChallenges").exists())
            .andExpect(jsonPath("$.percentage").exists())
            .andExpect(jsonPath("$.currentStreak").exists())
            .andExpect(jsonPath("$.longestStreak").exists())
    }

    @Test
    fun `should fail all endpoints without authentication`() {
        val updateRequest = UpdateUserRequestDto(
            name = "Name",
            email = "email@example.com",
            password = "password"
        )

        mockMvc.perform(
            put("/v1/users/$userId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isUnauthorized)

        mockMvc.perform(delete("/v1/users/$userId"))
            .andExpect(status().isUnauthorized)

        mockMvc.perform(get("/v1/users/$userId/challenges"))
            .andExpect(status().isUnauthorized)

        mockMvc.perform(get("/v1/users/$userId/challenges/$challengeId/submissions"))
            .andExpect(status().isUnauthorized)

        mockMvc.perform(get("/v1/users/$userId/statistics"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should get empty challenges list when user has no submissions`() {
        mockMvc.perform(
            get("/v1/users/$userId/challenges")
                .header("Authorization", "Bearer $authToken")
                .param("page", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(0))
    }

    @Test
    fun `should get empty submissions list when user has no submissions for challenge`() {
        mockMvc.perform(
            get("/v1/users/$userId/challenges/$challengeId/submissions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `should handle pagination correctly for user challenges`() {
        for (i in 1..15) {
            jdbcTemplate.execute("""
                INSERT INTO challenge (id, title, description, expected_pattern_id, published_at, created_at, is_daily)
                SELECT
                    UNHEX(REPLACE(UUID(), '-', '')) as id,
                    'Challenge $i' as title,
                    'Description $i' as description,
                    id as expected_pattern_id,
                    NOW() as published_at,
                    NOW() as created_at,
                    FALSE as is_daily
                FROM design_pattern
                WHERE name = 'Singleton'
                LIMIT 1
            """)

            val tempChallengeId = jdbcTemplate.queryForObject(
                "SELECT BIN_TO_UUID(id) FROM challenge WHERE title = ? LIMIT 1",
                String::class.java,
                "Challenge $i"
            )!!

            jdbcTemplate.execute("""
                INSERT INTO submission (id, user_id, challenge_id, code, language, pattern_id, submitted_at)
                VALUES (
                    UNHEX(REPLACE(UUID(), '-', '')),
                    UNHEX(REPLACE('$userId', '-', '')),
                    UNHEX(REPLACE('$tempChallengeId', '-', '')),
                    'code $i',
                    'java',
                    UNHEX(REPLACE('$patternId', '-', '')),
                    NOW()
                )
            """)
        }

        mockMvc.perform(
            get("/v1/users/$userId/challenges")
                .header("Authorization", "Bearer $authToken")
                .param("page", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(10))
            .andExpect(jsonPath("$.totalElements").value(15))

        mockMvc.perform(
            get("/v1/users/$userId/challenges")
                .header("Authorization", "Bearer $authToken")
                .param("page", "1")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(5))
            .andExpect(jsonPath("$.totalElements").value(15))
    }
}
