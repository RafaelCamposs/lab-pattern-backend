package com.example.user

import com.example.domain.submission.entity.Submission
import com.example.domain.user.usecase.GetUserStreaksUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class GetUserStreaksUseCaseTest {
    private val useCase = GetUserStreaksUseCase()

    @Test
    fun `should return zero streaks when submissions list is empty`() {
        val result = useCase.execute(emptyList())

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(0, streaks?.currentStreak)
        Assertions.assertEquals(0, streaks?.longestStreak)
    }

    @Test
    fun `should calculate longest streak correctly with consecutive days`() {
        val today = LocalDate.now()
        val submissions = listOf(
            createSubmission(today.minusDays(5)),
            createSubmission(today.minusDays(4)),
            createSubmission(today.minusDays(3)),
            createSubmission(today.minusDays(1)),
            createSubmission(today)
        )

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(3, streaks?.longestStreak)
    }

    @Test
    fun `should calculate current streak correctly when user submitted today`() {
        val today = LocalDate.now()
        val submissions = listOf(
            createSubmission(today.minusDays(3)),
            createSubmission(today.minusDays(2)),
            createSubmission(today.minusDays(1)),
            createSubmission(today)
        )

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(4, streaks?.currentStreak)
        Assertions.assertEquals(4, streaks?.longestStreak)
    }

    @Test
    fun `should calculate current streak correctly when user submitted yesterday`() {
        val today = LocalDate.now()
        val submissions = listOf(
            createSubmission(today.minusDays(3)),
            createSubmission(today.minusDays(2)),
            createSubmission(today.minusDays(1))
        )

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(3, streaks?.currentStreak)
        Assertions.assertEquals(3, streaks?.longestStreak)
    }

    @Test
    fun `should return zero current streak when last submission is more than one day ago`() {
        val today = LocalDate.now()
        val submissions = listOf(
            createSubmission(today.minusDays(5)),
            createSubmission(today.minusDays(4)),
            createSubmission(today.minusDays(3))
        )

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(0, streaks?.currentStreak)
        Assertions.assertEquals(3, streaks?.longestStreak)
    }

    @Test
    fun `should handle single submission correctly`() {
        val today = LocalDate.now()
        val submissions = listOf(createSubmission(today))

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(1, streaks?.currentStreak)
        Assertions.assertEquals(1, streaks?.longestStreak)
    }

    @Test
    fun `should handle multiple submissions on same day correctly`() {
        val today = LocalDate.now()
        val submissions = listOf(
            createSubmission(today.minusDays(2)),
            createSubmission(today.minusDays(1)),
            createSubmission(today.minusDays(1)),
            createSubmission(today),
            createSubmission(today)
        )

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(3, streaks?.currentStreak)
        Assertions.assertEquals(3, streaks?.longestStreak)
    }

    @Test
    fun `should calculate longest streak when there are multiple streaks`() {
        val today = LocalDate.now()
        val submissions = listOf(
            createSubmission(today.minusDays(10)),
            createSubmission(today.minusDays(9)),
            createSubmission(today.minusDays(8)),
            createSubmission(today.minusDays(7)),
            createSubmission(today.minusDays(5)),
            createSubmission(today.minusDays(4)),
            createSubmission(today.minusDays(1)),
            createSubmission(today)
        )

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(2, streaks?.currentStreak)
        Assertions.assertEquals(4, streaks?.longestStreak)
    }

    @Test
    fun `should return streak of 1 for non-consecutive submissions`() {
        val today = LocalDate.now()
        val submissions = listOf(
            createSubmission(today.minusDays(10)),
            createSubmission(today.minusDays(7)),
            createSubmission(today.minusDays(4)),
            createSubmission(today)
        )

        val result = useCase.execute(submissions)

        Assertions.assertTrue(result.isSuccess)
        val streaks = result.getOrNull()
        Assertions.assertNotNull(streaks)
        Assertions.assertEquals(1, streaks?.currentStreak)
        Assertions.assertEquals(1, streaks?.longestStreak)
    }

    private fun createSubmission(date: LocalDate): Submission {
        return Submission(
            id = UUID.randomUUID(),
            challengeId = UUID.randomUUID(),
            patternId = UUID.randomUUID(),
            code = "code",
            submittedAt = date.atStartOfDay(),
            language = "kotlin",
            userId = UUID.randomUUID(),
            evaluation = null
        )
    }
}
