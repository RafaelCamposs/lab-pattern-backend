package com.example.domain.user.entity.dto

import java.util.*

data class UpdateUserDto(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String
)