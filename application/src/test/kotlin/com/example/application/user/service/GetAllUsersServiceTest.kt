package com.example.application.user.service

import com.example.application.user.model.UserModel
import com.example.application.user.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class GetAllUsersServiceTest {
    private val userRepository: UserRepository = mockk()

    private val service = GetAllUsersService(userRepository)

    @Test
    fun `should get all users successfully`() {
        val userModels = listOf(
            UserModel(
                id = UUID.randomUUID(),
                name = "John Doe",
                email = "john@example.com",
                password = "password1"
            ),
            UserModel(
                id = UUID.randomUUID(),
                name = "Jane Doe",
                email = "jane@example.com",
                password = "password2"
            )
        )

        every { userRepository.findAll() } returns userModels

        val result = service.execute()

        Assertions.assertTrue(result.isSuccess)
        val users = result.getOrNull()
        Assertions.assertNotNull(users)
        Assertions.assertEquals(2, users?.size)
        Assertions.assertEquals(userModels[0].id, users?.get(0)?.id)
        Assertions.assertEquals(userModels[1].id, users?.get(1)?.id)
        verify { userRepository.findAll() }
    }

    @Test
    fun `should return empty list when no users exist`() {
        every { userRepository.findAll() } returns emptyList()

        val result = service.execute()

        Assertions.assertTrue(result.isSuccess)
        val users = result.getOrNull()
        Assertions.assertNotNull(users)
        Assertions.assertEquals(0, users?.size)
        verify { userRepository.findAll() }
    }

    @Test
    fun `should propagate repository error`() {
        val error = RuntimeException("Database error")

        every { userRepository.findAll() } throws error

        val result = service.execute()

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { userRepository.findAll() }
    }
}
