package com.example.application.pattern.service

import com.example.application.pattern.repository.DesignPatternRepository
import com.example.domain.pattern.entity.DesignPattern
import com.example.domain.pattern.gateway.GetPatternByIdGateway
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetPatternByIdService (
    private val patternRepository: DesignPatternRepository
): GetPatternByIdGateway {
    override fun execute(id: UUID): Result<DesignPattern> {
        return runCatching {
            patternRepository.findById(id).orElseThrow(
                { NoSuchElementException("Pattern not found") }
            ).toDomain()
        }
    }
}