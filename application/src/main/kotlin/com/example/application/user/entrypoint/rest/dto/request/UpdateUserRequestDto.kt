package com.example.application.user.entrypoint.rest.dto.request

import com.example.domain.user.entity.dto.UpdateUserDto

data class UpdateUserRequestDto(
    val name: String,
    val email: String,
    val password: String
) {
    fun toUpdateUserDto(id: Long) = UpdateUserDto(
        id = id,
        name = name,
        email = email,
        password = password
    )
}