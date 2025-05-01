package com.example.domain.pattern.usecase

import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.gateway.GetAllPatternsGateway
import jakarta.inject.Named

@Named
class GetAllPatternsUseCase (
    private val getAllPatternsGateway: GetAllPatternsGateway,
) {
    fun execute(): Result<List<DesignPattern>> {
        return getAllPatternsGateway.execute()
    }
}