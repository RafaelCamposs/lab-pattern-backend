package com.example.domain.user.entity

data class User(
    val id: Long? = null,
    val name: String,
    val email: String,
    val password: String
)