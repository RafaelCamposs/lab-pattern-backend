package com.example.application.service

import com.example.application.user.service.UserService
import com.example.domain.user.entity.User
import com.example.domain.user.usecase.UserUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class UserServiceTest {

    private lateinit var userUseCase: UserUseCase
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userUseCase = mock(UserUseCase::class.java)
        userService = UserService(userUseCase)
    }

    @Test
    fun testGetAllUsers() {
        val users = listOf(
            User(id = 1L, name = "John", email = "john@example.com", password = "password1"),
            User(id = 2L, name = "Jane", email = "jane@example.com", password = "password2")
        )
        
        `when`(userUseCase.getAllUsers()).thenReturn(users)
        
        val result = userService.getAllUsers()
        assertEquals(2, result.size)
        assertEquals("John", result[0].name)
        assertEquals("Jane", result[1].name)
        
        verify(userUseCase).getAllUsers()
    }
    
    @Test
    fun testGetUserById() {
        val user = User(id = 1L, name = "John", email = "john@example.com", password = "password1")
        
        `when`(userUseCase.getUserById(1L)).thenReturn(user)
        
        val result = userService.getUserById(1L)
        assertEquals("John", result?.name)
        
        verify(userUseCase).getUserById(1L)
    }
    
    @Test
    fun testCreateUser() {
        val newUser = User(name = "John", email = "john@example.com", password = "password1")
        val savedUser = User(id = 1L, name = "John", email = "john@example.com", password = "password1")
        
        `when`(userUseCase.createUser(newUser)).thenReturn(savedUser)
        
        val result = userService.createUser(newUser)
        assertEquals(1L, result.id)
        assertEquals("John", result.name)
        
        verify(userUseCase).createUser(newUser)
    }
}