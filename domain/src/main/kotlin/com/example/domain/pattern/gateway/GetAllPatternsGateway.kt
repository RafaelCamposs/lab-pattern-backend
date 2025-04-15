package com.example.domain.pattern.gateway

import com.example.domain.pattern.entity.DesignPattern

interface GetAllPatternsGateway {
    fun execute(): Result<List<DesignPattern>>
}