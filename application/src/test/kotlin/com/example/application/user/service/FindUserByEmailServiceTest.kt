package com.example.application.user.service

import com.example.application.user.model.UserModel
import com.example.application.user.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

class FindUserByEmailServiceTest {
    private val userRepository: UserRepository = mockk()

    private val service = FindUserByEmailService(userRepository)

    @Test
    fun `should find user by email successfully`() {
        val email = "john@example.com"
        val userModel = UserModel(
            id = UUID.randomUUID(),
            name = "John Doe",
            email = email,
            password = "password"
        )

        every { userRepository.findByEmail(email) } returns Optional.of(userModel)

        val result = service.execute(email)

        Assertions.assertTrue(result.isSuccess)
        val user = result.getOrNull()
        Assertions.assertNotNull(user)
        Assertions.assertEquals(userModel.id, user?.id)
        Assertions.assertEquals(userModel.name, user?.name)
        Assertions.assertEquals(userModel.email, user?.email)
        verify { userRepository.findByEmail(email) }
    }

    @Test
    fun `should return null when user not found by email`() {
        val email = "notfound@example.com"

        every { userRepository.findByEmail(email) } returns Optional.empty()

        val result = service.execute(email)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertNull(result.getOrNull())
        verify { userRepository.findByEmail(email) }
    }

    @Test
    fun `should propagate repository error`() {
        val email = "john@example.com"
        val error = RuntimeException("Database error")

        every { userRepository.findByEmail(email) } throws error

        val result = service.execute(email)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { userRepository.findByEmail(email) }
    }
}
