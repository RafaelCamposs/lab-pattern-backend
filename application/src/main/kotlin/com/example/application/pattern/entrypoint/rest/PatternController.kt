package com.example.application.pattern.entrypoint.rest

import com.example.application.pattern.entrypoint.rest.dto.response.PatternResponseDto
import com.example.domain.pattern.usecase.GetAllPatternsUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(allowedHeaders = ["*"], origins = ["*"])
@RestController
@RequestMapping("v1/patterns")
class PatternController (
    private val getAllPatternsUseCase: GetAllPatternsUseCase,
) {
    @GetMapping
    fun getAllPatterns(): ResponseEntity<List<PatternResponseDto>> {
        val patterns = getAllPatternsUseCase.execute().getOrThrow()
            .map { PatternResponseDto.fromDomain(it) }

        return ResponseEntity.ok(patterns)
    }
}