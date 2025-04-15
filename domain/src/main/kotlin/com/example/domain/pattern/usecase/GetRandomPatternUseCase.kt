package com.example.domain.pattern.usecase

import jakarta.inject.Named

@Named
class GetRandomPatternUseCase(
    private val getAllPatternsGateway: com.example.domain.pattern.gateway.GetAllPatternsGateway,
) {
    fun execute(): Result<com.example.domain.pattern.entity.DesignPattern> {
        return getAllPatternsGateway.execute().map { patterns ->
            patterns.random()
        }
    }
}