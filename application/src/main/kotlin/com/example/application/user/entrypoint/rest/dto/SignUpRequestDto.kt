
package com.example.application.user.entrypoint.rest.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignUpRequestDto(
    @field:NotBlank(message = "Nome de usuário é obrigatório")
    val username: String,

    @field:NotBlank(message = "Email é obrigatório")
    @field:Email(message = "Email deve ser válido")
    val email: String,

    @field:NotBlank(message = "Senha é obrigatória")
    val password: String
)
