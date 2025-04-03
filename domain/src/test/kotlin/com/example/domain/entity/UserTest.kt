package com.example.domain.entity

import com.example.domain.user.entity.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun testUserCreation() {
        val user = User(
            id = 1L,
            name = "John Doe",
            email = "john.doe@example.com",
            password = "password123"
        )
        
        assertEquals(1L, user.id)
        assertEquals("John Doe", user.name)
        assertEquals("john.doe@example.com", user.email)
        assertEquals("password123", user.password)
    }
    
    @Test
    fun testUserCopy() {
        val user = User(
            id = 1L,
            name = "John Doe",
            email = "john.doe@example.com",
            password = "password123"
        )
        
        val updatedUser = user.copy(name = "Jane Doe", email = "jane.doe@example.com")
        
        assertEquals(1L, updatedUser.id)
        assertEquals("Jane Doe", updatedUser.name)
        assertEquals("jane.doe@example.com", updatedUser.email)
        assertEquals("password123", updatedUser.password)
    }
}