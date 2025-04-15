package com.example.application.pattern.service

import com.example.application.pattern.repository.DesignPatternRepository
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.gateway.GetAllPatternsGateway
import org.springframework.stereotype.Component

@Component
class GetAllPatternsService (
    private val designPatternRepository: DesignPatternRepository,
) : GetAllPatternsGateway {
    override fun execute(): Result<List<DesignPattern>> {
        return kotlin.runCatching {
            designPatternRepository.findAll()
                .map { it.toDomain() }
        }
    }
}