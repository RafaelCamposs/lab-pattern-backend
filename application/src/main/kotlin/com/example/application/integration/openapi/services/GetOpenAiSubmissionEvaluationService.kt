package com.example.application.integration.openapi.services

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
    private val objectMapper: ObjectMapper
): EvaluateSubmissionWithAiGateway {

    override fun execute(
        pattern: DesignPattern,
        challenge: Challenge,
        submission: Submission
    ): Result<AiEvaluationDto> {
        val prompt = """"
            You are an expert software engineer specializing in design patterns. 
            
            Evaluate the following solution for a design pattern problem. 
            Problem Title: ${challenge.title}
            Problem Description: ${challenge.description} 
            Expected Pattern: ${pattern.name}
            Description: ${pattern.description}
            Language: ${submission.language}
            Code Solution: ${submission.code}
              
            Please evaluate the solution based on: 
            1. Correct implementation of the ${pattern.name} pattern 
            2. Code quality and best practices 
            3. Solution effectiveness for the given problem
            
            Provide a detailed evaluation in the following JSON structure without the json markdown indicator: 
            { 
                "score": [A number between 0-100],
                "feedback": [Array of general feedback points],
                "strengths": [Array of specific strengths],
                "improvements": [Array of specific areas for improvement] 
            }                  
        """

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
            objectMapper.readValue(jsonResponse, OpenAiEvaluationResponseDto::class.java).toDomain()
        }
    }

}