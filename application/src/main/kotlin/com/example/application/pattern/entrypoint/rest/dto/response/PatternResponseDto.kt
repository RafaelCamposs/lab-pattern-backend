package com.example.application.pattern.entrypoint.rest.dto.response

import com.example.domain.pattern.entity.DesignPattern
import java.util.*

data class PatternResponseDto(
    val id: UUID,
    val name: String,
) {
    companion object {
        fun fromDomain(pattern: DesignPattern): PatternResponseDto {
            return PatternResponseDto(
                id = pattern.id,
                name = pattern.name,
            )
        }
    }
}
