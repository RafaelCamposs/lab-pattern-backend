package com.example.application.pattern.model

import com.example.domain.pattern.entity.DesignPattern
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "design_pattern")
data class DesignPatternModel(
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    val id: UUID? = null,

    val name: String,

    val description: String,

    val category: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toDomain() = DesignPattern(
        id = id ?: UUID.randomUUID(),
        name = name,
        description = description,
        category = category
    )
}
