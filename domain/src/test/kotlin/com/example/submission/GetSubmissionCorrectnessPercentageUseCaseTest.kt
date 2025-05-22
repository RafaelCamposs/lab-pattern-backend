package com.example.submission

import com.example.domain.evaluation.entity.Evaluation 
import com.example.domain.evaluation.entity.Feedback
import com.example.domain.submission.entity.Submission
import com.example.domain.submission.usecase.GetSubmissionCorrectnessPercentageUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetSubmissionCorrectnessPercentageUseCaseTest {
    private val useCase = GetSubmissionCorrectnessPercentageUseCase()

    @Test
    fun `should calculate average score successfully`() {
        val submissions = listOf(
            createSubmission(score = 80),
            createSubmission(score = 90),
            createSubmission(score = 100)
        )

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(90, result.getOrNull())
    }

    @Test 
    fun `should return zero for empty submissions list`() {
        val result = useCase.execute(emptyList())

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(0, result.getOrNull())
    }

    private fun createSubmission(score: Int) = Submission(
        id = UUID.randomUUID(),
        challengeId = UUID.randomUUID(),
        patternId = UUID.randomUUID(),
        code = "code",
        submittedAt = LocalDateTime.now(),
        language = "kotlin",
        userId = UUID.randomUUID(),
        evaluation = Evaluation(
            id = UUID.randomUUID(),
            submissionId = UUID.randomUUID(),
            score = score,
            feedback = Feedback(
                content = listOf(),
                improvements = listOf(),
                strengths = listOf()
            ),
            evaluatedAt = LocalDateTime.now()
        )
    )
}