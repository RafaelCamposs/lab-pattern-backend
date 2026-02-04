package com.example.application.integration.openapi.services

import com.example.application.integration.openapi.config.OpenAiPromptConfig
import com.example.application.integration.openapi.dto.response.OpenApiChallengeResponseDto
import com.example.domain.challenge.entity.dto.AiQuestionDto
import com.example.domain.challenge.gateway.GetAiQuestionGateway
import com.example.domain.pattern.entity.DesignPattern
import com.fasterxml.jackson.databind.ObjectMapper
import com.openai.client.OpenAIClient
import com.openai.models.responses.ResponseCreateParams
import org.springframework.stereotype.Component

@Component
class GetOpenAiQuestionService(
    private val openAiClient: OpenAIClient,
    private val promptConfig: OpenAiPromptConfig,
    private val objectMapper: ObjectMapper
) : GetAiQuestionGateway {

    override fun execute(pattern: DesignPattern, theme: String): Result<AiQuestionDto> {
        return runCatching {
            val prompt = promptConfig.getChallengePrompt(
                pattern,
                theme
            )

            val params = ResponseCreateParams
                .builder()
                .input(prompt)
                .model("gpt-5.2-chat-latest")
                .build()

            val openApiResponse = openAiClient.responses().create(params)

            val textContent = openApiResponse.output()
                .first { it.message().isPresent }
                .message().get()
                .content()
                .first { it.outputText().isPresent }
                .outputText().get()
                .text()

            objectMapper.readValue(textContent, OpenApiChallengeResponseDto::class.java).toDomain()
        }
    }
}