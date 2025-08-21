package com.example.application.config

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAiConfig(
    @param:Value($$"${open-ai.api-key}")
    private val openAiApiKey: String,
    @param:Value($$"${open-ai.organization-id}")
    private val openAiOrganizationId: String,
    @param:Value($$"${open-ai.project-id}")
    private val openAiProjectId: String,
) {
    @Bean
    fun openAiClient(): OpenAIClient {
        return OpenAIOkHttpClient.builder()
            .apiKey(openAiApiKey)
            .organization(openAiOrganizationId)
            .project(openAiProjectId)
            .build()
    }
}