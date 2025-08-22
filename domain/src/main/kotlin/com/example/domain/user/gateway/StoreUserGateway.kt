package com.example.domain.user.gateway

import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.StoreUserDto

interface StoreUserGateway {
    fun execute(storeUserDto : StoreUserDto): Result<User>
}