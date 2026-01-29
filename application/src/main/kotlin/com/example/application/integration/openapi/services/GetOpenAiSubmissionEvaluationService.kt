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
import com.openai.models.ChatModel
import com.openai.models.chat.completions.ChatCompletionCreateParams
import org.springframework.stereotype.Component

@Component
class GetOpenAiSubmissionEvaluationService (
    private val openAiClient: OpenAIClient,
    private val promptConfig: OpenAiPromptConfig,
    private val objectMapper: ObjectMapper
): EvaluateSubmissionWithAiGateway {

    override fun execute(
        pattern: DesignPattern,
        challenge: Challenge,
        submission: Submission
    ): Result<AiEvaluationDto> {
        return runCatching {
            val prompt = promptConfig.getSubmissionEvaluationPrompt(
                pattern,
                challenge,
                submission
            )

            val params = ChatCompletionCreateParams
                .builder()
                .model(ChatModel.GPT_5_CHAT_LATEST)
                .addUserMessage(
                    prompt
                )
                .build()

            val openApiResponse = openAiClient.chat().completions().create(params)

            val jsonResponse =  openApiResponse.choices()[0].message().content().get()
            objectMapper.readValue(jsonResponse, OpenAiEvaluationResponseDto::class.java).toDomain()
        }
    }

}