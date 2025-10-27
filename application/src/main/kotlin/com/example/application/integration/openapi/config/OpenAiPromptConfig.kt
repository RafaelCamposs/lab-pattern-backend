package com.example.application.integration.openapi.config

import com.example.application.utils.PromptLoader
import com.example.domain.challenge.entity.Challenge
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.submission.entity.Submission
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAiPromptConfig(
    private val promptLoader: PromptLoader
) {
    private val challengePrompt: String = promptLoader.loadPrompt("challenge-generation-improved")
    private val evaluationPrompt: String = promptLoader.loadPrompt("submission-evaluation-improved")

    fun getChallengePrompt(pattern: DesignPattern, theme: String): String {
        return challengePrompt
            .replace("\${pattern.name}", pattern.name)
            .replace("\${pattern.description}", pattern.description)
            .replace("\${theme}", theme)
    }

    fun getSubmissionEvaluationPrompt(
        pattern: DesignPattern,
        challenge: Challenge,
        submission: Submission
    ): String {
        return evaluationPrompt
            .replace("\${pattern.name}", pattern.name)
            .replace("\${pattern.description}", pattern.description)
            .replace("\${challenge.title}", challenge.title)
            .replace("\${challenge.description}", challenge.description)
            .replace("\${submission.language}", submission.language)
            .replace("\${submission.code}", submission.code)
    }
}