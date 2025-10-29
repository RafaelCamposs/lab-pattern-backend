package com.example.domain.user.usecase

import com.example.domain.submission.entity.Submission
import com.example.domain.user.entity.dto.StreaksDto
import jakarta.inject.Named
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Named
class GetUserStreaksUseCase {
    fun execute(submissions: List<Submission>): Result<StreaksDto> {
        return runCatching {
            if (submissions.isEmpty()) {
                return@runCatching StreaksDto(currentStreak = 0, longestStreak = 0)
            }

            val submissionDates = submissions
                .map { it.submittedAt.toLocalDate() }
                .distinct()
                .sorted()

            val maxStreak = calculateMaxStreak(submissionDates)
            val currentStreak = calculateCurrentStreak(submissionDates)

            StreaksDto(currentStreak = currentStreak, longestStreak = maxStreak)
        }
    }

    private fun calculateMaxStreak(submissionDates: List<LocalDate>): Int {
        if (submissionDates.isEmpty()) return 0

        var maxStreak = 1
        var currentStreak = 1

        for (i in 1 until submissionDates.size) {
            val diff = ChronoUnit.DAYS.between(submissionDates[i - 1], submissionDates[i])
            if (diff == 1L) {
                currentStreak++
                maxStreak = maxOf(maxStreak, currentStreak)
            } else {
                currentStreak = 1
            }
        }

        return maxStreak
    }

    private fun calculateCurrentStreak(submissionDates: List<LocalDate>): Int {
        if (submissionDates.isEmpty()) return 0

        val today = LocalDate.now()
        val lastSubmission = submissionDates.last()

        val daysSinceLastSubmission = ChronoUnit.DAYS.between(lastSubmission, today)
        if (daysSinceLastSubmission > 1) {
            return 0
        }

        var currentStreak = 1
        var expectedDate = lastSubmission.minusDays(1)

        for (i in submissionDates.size - 2 downTo 0) {
            if (submissionDates[i] == expectedDate) {
                currentStreak++
                expectedDate = expectedDate.minusDays(1)
            } else {
                break
            }
        }

        return currentStreak
    }
}