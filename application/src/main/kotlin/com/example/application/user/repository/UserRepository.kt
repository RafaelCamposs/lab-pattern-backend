package com.example.application.user.repository

import com.example.application.user.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserModel, UUID> {
    fun findByEmail(email: String): Optional<UserModel>
}