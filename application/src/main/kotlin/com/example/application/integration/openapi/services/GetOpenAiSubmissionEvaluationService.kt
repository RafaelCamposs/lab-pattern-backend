package com.example.application.integration.openapi.services

import com.example.application.integration.openapi.config.OpenAiPromptConfig
import com.example.application.integration.openapi.dto.response.OpenAiEvaluationResponseDto
import com.example.domain.challenge.entity.Challenge
import com.example.domain.evaluation.entity.dto.AiEvaluationDto
import com.example.domain.evaluation.gateway.EvaluateSubmissionWithAiGateway
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.submission.entity.Submission
import com.fasterxml.jackson.databind.ObjectMapper
import com.openai.client.OpenAIClient
import com.openai.models.responses.ResponseCreateParams
import org.springframework.stereotype.Component

@Component
class GetOpenAiSubmissionEvaluationService (
    private val openAiClient: OpenAIClient,
    private val promptConfig: OpenAiPromptConfig,
    private val objectMapper: ObjectMapper
): EvaluateSubmissionWithAiGateway {

    override fun execute(
        selectedPattern: DesignPattern,
        expectedPattern: DesignPattern,
        challenge: Challenge,
        submission: Submission
    ): Result<AiEvaluationDto> {
        return runCatching {
            val prompt = promptConfig.getSubmissionEvaluationPrompt(
                selectedPattern,
                expectedPattern,
                challenge,
                submission
            )

            val params = ResponseCreateParams
                .builder()
                .input(prompt)
                .model("gpt-5.2-codex")
                .build()

            val openApiResponse = openAiClient.responses().create(params)

            val jsonResponse = openApiResponse.output()
                .first { it.message().isPresent }
                .message().get()
                .content()
                .first { it.outputText().isPresent }
                .outputText().get()
                .text()

            objectMapper.readValue(jsonResponse, OpenAiEvaluationResponseDto::class.java).toDomain()
        }
    }

}