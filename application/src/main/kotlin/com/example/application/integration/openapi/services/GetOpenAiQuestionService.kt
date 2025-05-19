package com.example.application.integration.openapi.services

import com.example.application.integration.openapi.config.OpenAiPromptConfig
import com.example.application.integration.openapi.dto.response.OpenApiChallengeResponseDto
import com.example.domain.challenge.entity.dto.AiQuestionDto
import com.example.domain.challenge.gateway.GetAiQuestionGateway
import com.example.domain.pattern.entity.DesignPattern
import com.fasterxml.jackson.databind.ObjectMapper
import com.openai.client.OpenAIClient
import com.openai.models.ChatModel
import com.openai.models.chat.completions.ChatCompletionCreateParams
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

            val params = ChatCompletionCreateParams
                .builder()
                .model(ChatModel.GPT_4O_MINI)
                .addUserMessage(
                    prompt
                )
                .build()

            val openApiResponse = openAiClient.chat().completions().create(params)

            val jsonResponse =  openApiResponse.choices()[0].message().content().get()
            objectMapper.readValue(jsonResponse, OpenApiChallengeResponseDto::class.java).toDomain()
        }
    }
}