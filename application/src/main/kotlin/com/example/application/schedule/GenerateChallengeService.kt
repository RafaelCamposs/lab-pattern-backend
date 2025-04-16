package com.example.application.schedule

import com.example.application.integration.openapi.services.GetOpenAiQuestionService
import com.example.domain.challenge.entity.dto.StoreChallengeDto
import com.example.domain.challenge.usecase.StoreChallengeUseCase
import com.example.domain.pattern.usecase.GetRandomPatternUseCase
import com.example.domain.pattern.usecase.GetRandomThemeUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class GenerateChallengeService (
    private val getRandomPatternUseCase: GetRandomPatternUseCase,
    private val getRandomThemeUseCase: GetRandomThemeUseCase,
    private val storeChallengeUseCase: StoreChallengeUseCase,
    private val getOpenAiQuestionService: GetOpenAiQuestionService,
) {
    @Scheduled(fixedRate = 60000)
    fun execute() {
        val patternResult = getRandomPatternUseCase.execute().getOrThrow()
        val themeResult = getRandomThemeUseCase.execute().getOrThrow()

        val response = getOpenAiQuestionService.execute(patternResult, themeResult).getOrThrow()

        val storeChallengeDto = StoreChallengeDto(
            expectedPatternId = patternResult.id,
            title = "",
            description = "",
        )

        storeChallengeUseCase.execute(storeChallengeDto).onFailure {
            println("Failed to store challenge: ${it.message}")
        }.onSuccess {
            println("Successfully stored challenge: $it")
        }

        println("Scheduled task executed: $patternResult, $themeResult")
    }
}