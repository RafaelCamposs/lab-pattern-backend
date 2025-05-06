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
            val submissionDates = submissions.map { it.submittedAt.toLocalDate() }
            val maxStreak = calculateMaxStreak(submissionDates)
            val currentStreak = calculateCurrentStreak(submissionDates)

            StreaksDto(maxStreak, currentStreak)
        }
    }

    private fun calculateMaxStreak(submissionDates: List<LocalDate>): Int {
        var maxStreak = 0
        var tempStreak = 1

        for (i in 1 until submissionDates.size) {
            val diff = ChronoUnit.DAYS.between(submissionDates[i - 1], submissionDates[i])
            if (diff == 1L) {
                tempStreak++
            } else if (diff > 1) {
                maxStreak = maxOf(maxStreak, tempStreak)
                tempStreak = 1
            }
        }

        return maxOf(maxStreak, tempStreak)
    }

    private fun calculateCurrentStreak(submissionDates: List<LocalDate>): Int {
        var currentStreak = 0

        for (i in submissionDates.indices.reversed()) {
            val expected = LocalDate.now().minusDays((submissionDates.size - 1 - i).toLong())
            if (submissionDates[i] == expected) {
                currentStreak++
            } else {
                break
            }
        }

        return currentStreak
    }
}