package com.example.submission

import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.Feedback
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.gateway.GetSubmissionByIdGateway
import com.example.domain.submission.usecase.GetSubmissionByIdUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetSubmissionByIdUseCaseTest {
    private val getSubmissionByIdGateway: GetSubmissionByIdGateway = mockk()
    private val useCase = GetSubmissionByIdUseCase(getSubmissionByIdGateway)

    @Test
    fun `should get submission by id successfully`() {
        val id = UUID.randomUUID()
        val submission = Submission(
            id = id,
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code",
            submittedAt = LocalDateTime.now(),
            language = "kotlin",
            userId = UUID.randomUUID(),
            evaluation = Evaluation(
                id = UUID.randomUUID(),
                submissionId = id,
                score = 100,
                feedback = Feedback(
                    content = listOf(),
                    improvements = listOf(),
                    strengths = listOf()
                ),
                evaluatedAt = LocalDateTime.now()
            )
        )
        every { getSubmissionByIdGateway.execute(id) } returns Result.success(submission)

        val result = useCase.execute(id)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(submission, result.getOrNull())
        verify { getSubmissionByIdGateway.execute(id) }
    }

    @Test
    fun `should propagate gateway error`() {
        val id = UUID.randomUUID()
        val error = RuntimeException("Gateway error")
        every { getSubmissionByIdGateway.execute(id) } returns Result.failure(error)

        val result = useCase.execute(id)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { getSubmissionByIdGateway.execute(id) }
    }
}