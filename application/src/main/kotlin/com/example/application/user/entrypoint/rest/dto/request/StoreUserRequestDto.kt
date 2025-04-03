package com.example.application.user.entrypoint.rest.dto.request

import com.example.domain.user.entity.dto.StoreUserDto

data class StoreUserRequestDto(
    val name: String,
    val email: String,
    val password: String,
) {
    fun toStoreUserDto() = StoreUserDto(
        name = name,
        email = email,
        password = password
    )
}
