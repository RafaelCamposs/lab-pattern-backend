package com.example.domain.user.entity

import java.util.*

data class User(
    val id: UUID? = null,
    val name: String,
    val email: String,
    val password: String
)