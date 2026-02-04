package com.example.application.config

import com.example.application.integration.openapi.services.GetOpenAiQuestionService
import com.example.application.integration.openapi.services.GetOpenAiSubmissionEvaluationService
import com.example.domain.challenge.entity.dto.AiQuestionDto
import com.example.domain.evaluation.entity.dto.AiEvaluationDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MySQLContainer

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.task.scheduling.enabled=false"]
)
@AutoConfigureMockMvc
abstract class BaseIntegrationTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var jdbcTemplate: JdbcTemplate

    companion object {
        private val mysqlContainer = MySQLContainer<Nothing>("mysql:8.0").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
            withReuse(true)
        }

        init {
            mysqlContainer.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", mysqlContainer::getUsername)
            registry.add("spring.datasource.password", mysqlContainer::getPassword)
            registry.add("spring.flyway.enabled") { "true" }
            registry.add("jwt.secret-key") { "dGVzdHNlY3JldGtleWZvcmludGVncmF0aW9udGVzdHNtdXN0YmVsb25nZW5vdWdoZm9ySldU" }
            registry.add("open-ai.api-key") { "test-api-key" }
            registry.add("open-ai.project-id") { "test-project-id" }
            registry.add("open-ai.organization-id") { "test-org-id" }
        }
    }

    @BeforeEach
    fun cleanDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0")
        jdbcTemplate.execute("TRUNCATE TABLE evaluation")
        jdbcTemplate.execute("TRUNCATE TABLE submission")
        jdbcTemplate.execute("TRUNCATE TABLE challenge")
        jdbcTemplate.execute("TRUNCATE TABLE user_progress")
        jdbcTemplate.execute("TRUNCATE TABLE user")
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1")
    }

    @TestConfiguration
    class TestConfig {

        @Bean
        @Primary
        fun mockGetOpenAiQuestionService(): GetOpenAiQuestionService {
            val mock = mockk<GetOpenAiQuestionService>()
            every { mock.execute(any(), any()) } returns Result.success(
                AiQuestionDto(
                    title = "Test Challenge Title",
                    description = "Test challenge description for design pattern"
                )
            )
            return mock
        }

        @Bean
        @Primary
        fun mockGetOpenAiSubmissionEvaluationService(): GetOpenAiSubmissionEvaluationService {
            val mock = mockk<GetOpenAiSubmissionEvaluationService>()
            every { mock.execute(any(), any(), any(), any()) } returns Result.success(
                AiEvaluationDto(
                    score = 85,
                    feedback = listOf("Good implementation of the design pattern"),
                    strengths = listOf("Clean code", "Well structured"),
                    improvements = listOf("Could add more comments")
                )
            )
            return mock
        }
    }
}
