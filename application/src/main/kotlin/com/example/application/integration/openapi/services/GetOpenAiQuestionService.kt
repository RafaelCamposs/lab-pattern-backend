package com.example.application.integration.openapi.services

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
    private val objectMapper: ObjectMapper
) : GetAiQuestionGateway {

    override fun execute(pattern: DesignPattern, theme: String): Result<AiQuestionDto> {
        val prompt = """
            You are a design pattern professor tasked with guiding students on identifying real-world
             scenarios
             
            - Generate a real-world problem where the ${pattern.name} pattern is applicable.
            - The problem should be relevant to the ${pattern.name} pattern.
            - The problem should be related to the theme: $theme.
            - Provide a formal description of the scenario in Brazilian Portuguese.
            - Begin by setting the system's context and core functionalities.
            - Describe the challenge faced by the engineering or project team.
            
            **Requirements:**
            
            - Do not specify the design pattern to use.
            - Provide no more than two subtle hints regarding the solution.
            - Avoid highlighting any text or phrase.
            - Limit the text to a maximum of four paragraphs.
            
            # Output Format
            
            Please output the response with the following JSON structure:
            
            {
              "description": "[The detailed problem scenario here]",
              "title": "[A concise summary of the challenge here]"
            }
            
            # Notes 
            
            - Ensure the problem context is relevant to scenarios suited for the ${pattern.name} pattern, such as {${pattern.description}}.
            - Maintain clarity in describing the context and problem without explicitly naming the design pattern.
        """.trimIndent()

        return runCatching {
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