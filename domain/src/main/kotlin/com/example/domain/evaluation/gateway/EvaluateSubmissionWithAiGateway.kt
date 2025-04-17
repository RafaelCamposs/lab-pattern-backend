package com.example.domain.evaluation.gateway

import com.example.domain.challenge.entity.Challenge
import com.example.domain.evaluation.entity.dto.AiEvaluationDto
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.submission.entity.Submission

interface EvaluateSubmissionWithAiGateway {
    fun execute(
        pattern: DesignPattern,
        challenge: Challenge,
        submission: Submission
    ): Result<AiEvaluationDto>
}