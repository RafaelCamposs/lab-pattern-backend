package com.example.domain.challenge.gateway

import com.example.domain.challenge.entity.dto.AiQuestionDto
import com.example.domain.pattern.entity.DesignPattern

interface GetAiQuestionGateway {
    fun execute(pattern: DesignPattern, theme: String): Result<AiQuestionDto>
}