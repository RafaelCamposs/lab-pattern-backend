package com.example.domain.pattern.gateway

import com.example.domain.pattern.entity.DesignPattern
import java.util.UUID

interface GetPatternByIdGateway {
    fun execute(id: UUID): Result<DesignPattern>
}