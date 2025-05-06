package com.example.domain.submission.usecase

import com.example.domain.submission.entity.Submission
import jakarta.inject.Named

@Named
class GetSubmissionCorrectnessPercentageUseCase {
    fun execute(submissions: List<Submission>): Result<Int> {
        val totalSubmissions = submissions.size
        if (totalSubmissions == 0) {
            return Result.success(0)
        }

        val totalScore = submissions.sumOf { it.evaluation!!.score }
        val averageScore = totalScore / totalSubmissions

        return Result.success(averageScore)
    }
}