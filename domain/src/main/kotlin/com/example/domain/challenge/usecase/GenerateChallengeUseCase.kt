package com.example.domain.challenge.usecase

import com.example.domain.challenge.entity.Challenge
import com.example.domain.challenge.entity.dto.StoreChallengeDto
import com.example.domain.challenge.gateway.GetAiQuestionGateway
import com.example.domain.challenge.gateway.StoreChallengeGateway
import com.example.domain.pattern.usecase.GetRandomPatternUseCase
import com.example.domain.pattern.usecase.GetRandomThemeUseCase
import jakarta.inject.Named

@Named
class GenerateChallengeUseCase (
    private val getAiQuestionGateway: GetAiQuestionGateway,
    private val storeChallengeGateway: StoreChallengeGateway,
    private val getRandomPatternUseCase: GetRandomPatternUseCase,
    private val getRandomThemeUseCase: GetRandomThemeUseCase,
) {
    fun execute(): Result<Challenge> {
        return runCatching {
            val patternResult = getRandomPatternUseCase.execute().getOrThrow()
            val themeResult = getRandomThemeUseCase.execute().getOrThrow()

            val openAiChallengeResponseDto = getAiQuestionGateway.execute(patternResult, themeResult).getOrThrow()

            val storeChallengeDto = StoreChallengeDto(
                expectedPatternId = patternResult.id,
                title = openAiChallengeResponseDto.title,
                description = openAiChallengeResponseDto.description,
            )

            storeChallengeGateway.execute(storeChallengeDto).getOrThrow()
        }
    }
}