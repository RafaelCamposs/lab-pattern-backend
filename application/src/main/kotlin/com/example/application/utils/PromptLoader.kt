package com.example.application.utils

import com.example.application.exception.PromptLoadException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PromptLoader {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun loadPrompt(fileName: String): String {
        return try {
            val resource = javaClass.classLoader.getResourceAsStream("prompts/$fileName.md")
                ?: throw IllegalArgumentException("Prompt file $fileName.md not found")
            
            resource.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            logger.error("Failed to load prompt file $fileName.md: ${e.message}")
            throw PromptLoadException("Failed to load prompt: ${e.message}") as Throwable
        }
    }
}