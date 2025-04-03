package com.example.domain.user.entity.dto

data class UpdateUserDto(
    val id: Long,
    val name: String,
    val email: String,
    val password: String
)