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

class GetUserByIdServiceTest {
    private val userRepository: UserRepository = mockk()

    private val service = GetUserByIdService(userRepository)

    @Test
    fun `should get user by id successfully`() {
        val userId = UUID.randomUUID()
        val userModel = UserModel(
            id = userId,
            name = "John Doe",
            email = "john@example.com",
            password = "password"
        )

        every { userRepository.findById(userId) } returns Optional.of(userModel)

        val result = service.execute(userId)

        Assertions.assertTrue(result.isSuccess)
        val user = result.getOrNull()
        Assertions.assertNotNull(user)
        Assertions.assertEquals(userModel.id, user?.id)
        Assertions.assertEquals(userModel.name, user?.name)
        Assertions.assertEquals(userModel.email, user?.email)
        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should fail when user not found by id`() {
        val userId = UUID.randomUUID()

        every { userRepository.findById(userId) } returns Optional.empty()

        val result = service.execute(userId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is NoSuchElementException)
        Assertions.assertEquals("User not found", result.exceptionOrNull()?.message)
        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should propagate repository error`() {
        val userId = UUID.randomUUID()
        val error = RuntimeException("Database error")

        every { userRepository.findById(userId) } throws error

        val result = service.execute(userId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { userRepository.findById(userId) }
    }
}
