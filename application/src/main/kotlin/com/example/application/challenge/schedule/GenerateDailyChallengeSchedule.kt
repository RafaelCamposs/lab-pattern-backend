package com.example.application.challenge.schedule

import com.example.application.integration.openapi.services.GetOpenAiQuestionService
import com.example.domain.challenge.entity.dto.StoreChallengeDto
import com.example.domain.challenge.usecase.StoreChallengeUseCase
import com.example.domain.pattern.usecase.GetRandomPatternUseCase
import com.example.domain.pattern.usecase.GetRandomThemeUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class GenerateDailyChallengeSchedule (
    private val getRandomPatternUseCase: GetRandomPatternUseCase,
    private val getRandomThemeUseCase: GetRandomThemeUseCase,
    private val storeChallengeUseCase: StoreChallengeUseCase,
    private val getOpenAiQuestionService: GetOpenAiQuestionService,
) {
    @Scheduled(cron = "0 0 8 * * *")
    fun execute() {
        val patternResult = getRandomPatternUseCase.execute().getOrThrow()
        val themeResult = getRandomThemeUseCase.execute().getOrThrow()

        val openApiChallengeResponseDto = getOpenAiQuestionService.execute(patternResult, themeResult).getOrThrow()

        val storeChallengeDto = StoreChallengeDto(
            expectedPatternId = patternResult.id,
            title = openApiChallengeResponseDto.title,
            description = openApiChallengeResponseDto.description,
            isDaily = true
        )

        storeChallengeUseCase.execute(storeChallengeDto).onFailure {
            println("Failed to store challenge: ${it.message}")
        }.onSuccess {
            println("Successfully stored challenge: $it")
        }

        println("Scheduled task executed: $patternResult, $themeResult")
    }
}