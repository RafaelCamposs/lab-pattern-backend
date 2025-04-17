package com.example.domain.pattern.usecase

import com.example.domain.pattern.entity.DesignPattern
import jakarta.inject.Named

@Named
class GetAllPatternsUseCase (
    private val getAllPatternsGateway: com.example.domain.pattern.gateway.GetAllPatternsGateway,
) {
    fun execute(): Result<List<DesignPattern>> {
        return getAllPatternsGateway.execute()
    }
}