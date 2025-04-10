package com.example.domain.team.entity.dto

data class StoreTeamDto(
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val createdById: Long
)
