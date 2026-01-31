package com.example.user

import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.StoreUserDto
import com.example.domain.user.gateway.FindUserByEmailGateway
import com.example.domain.user.gateway.StoreUserGateway
import com.example.domain.user.usecase.StoreUserUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class StoreUserUseCaseTest {
    private val findUserByEmailGateway: FindUserByEmailGateway = mockk()
    private val storeUserGateway: StoreUserGateway = mockk()

    private val useCase = StoreUserUseCase(
        findUserByEmailGateway,
        storeUserGateway
    )

    @Test
    fun `should store user successfully when email does not exist`() {
        val storeUserDto = StoreUserDto(
            name = "John Doe",
            email = "john@example.com",
            password = "password123"
        )

        val user = User(
            id = UUID.randomUUID(),
            name = storeUserDto.name,
            email = storeUserDto.email,
            password = storeUserDto.password
        )

        every { findUserByEmailGateway.execute(storeUserDto.email) } returns Result.success(null)
        every { storeUserGateway.execute(storeUserDto) } returns Result.success(user)

        val result = useCase.execute(storeUserDto)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(user, result.getOrNull())
        verify { findUserByEmailGateway.execute(storeUserDto.email) }
        verify { storeUserGateway.execute(storeUserDto) }
    }

    @Test
    fun `should fail when user already exists`() {
        val storeUserDto = StoreUserDto(
            name = "John Doe",
            email = "john@example.com",
            password = "password123"
        )

        val existingUser = User(
            id = UUID.randomUUID(),
            name = "Existing User",
            email = storeUserDto.email,
            password = "oldpassword"
        )

        every { findUserByEmailGateway.execute(storeUserDto.email) } returns Result.success(existingUser)

        val result = useCase.execute(storeUserDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        Assertions.assertEquals("Usuário já existe", result.exceptionOrNull()?.message)
        verify { findUserByEmailGateway.execute(storeUserDto.email) }
        verify(exactly = 0) { storeUserGateway.execute(any()) }
    }

    @Test
    fun `should propagate find user gateway error`() {
        val storeUserDto = StoreUserDto(
            name = "John Doe",
            email = "john@example.com",
            password = "password123"
        )

        val error = RuntimeException("Database error")

        every { findUserByEmailGateway.execute(storeUserDto.email) } returns Result.failure(error)

        Assertions.assertThrows(RuntimeException::class.java) {
            useCase.execute(storeUserDto)
        }

        verify { findUserByEmailGateway.execute(storeUserDto.email) }
        verify(exactly = 0) { storeUserGateway.execute(any()) }
    }

    @Test
    fun `should propagate store user gateway error`() {
        val storeUserDto = StoreUserDto(
            name = "John Doe",
            email = "john@example.com",
            password = "password123"
        )

        val error = RuntimeException("Storage error")

        every { findUserByEmailGateway.execute(storeUserDto.email) } returns Result.success(null)
        every { storeUserGateway.execute(storeUserDto) } returns Result.failure(error)

        val result = useCase.execute(storeUserDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { findUserByEmailGateway.execute(storeUserDto.email) }
        verify { storeUserGateway.execute(storeUserDto) }
    }
}
