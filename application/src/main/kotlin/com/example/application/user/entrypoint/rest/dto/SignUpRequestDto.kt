
package com.example.application.user.entrypoint.rest.dto

data class SignUpRequestDto(
    val username: String,
    val email: String,
    val password: String
)
