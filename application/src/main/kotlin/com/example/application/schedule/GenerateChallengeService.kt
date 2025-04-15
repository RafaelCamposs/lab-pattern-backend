package com.example.application.schedule

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
) {
    @Scheduled(fixedRate = 60000)
    fun execute() {

        val patternResult = getRandomPatternUseCase.execute().getOrThrow()
        val themeResult = getRandomThemeUseCase.execute().getOrThrow()

        val storeChallengeDto = StoreChallengeDto(
            expectedPatternId = patternResult.id,
            title = "${themeResult} ${patternResult.name}",
            description = "${themeResult} ${patternResult.description}",
        )

        storeChallengeUseCase.execute(storeChallengeDto).onFailure {
            println("Failed to store challenge: ${it.message}")
        }.onSuccess {
            println("Successfully stored challenge: $it")
        }

        println("Scheduled task executed: $patternResult, $themeResult")
    }
}