package com.example.application.user.service

import com.example.application.user.model.UserModel
import com.example.application.user.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

class DeleteUserServiceTest {
    private val userRepository: UserRepository = mockk()

    private val service = DeleteUserService(userRepository)

    @Test
    fun `should delete user successfully by setting deletedAt`() {
        val userId = UUID.randomUUID()
        val userModel = UserModel(
            id = userId,
            name = "John Doe",
            email = "john@example.com",
            password = "password",
            deletedAt = null
        )

        val deletedUserSlot = slot<UserModel>()

        every { userRepository.existsById(userId) } returns true
        every { userRepository.findById(userId) } returns Optional.of(userModel)
        every { userRepository.save(capture(deletedUserSlot)) } returns userModel.copy(deletedAt = LocalDateTime.now())

        val result = service.execute(userId)

        Assertions.assertTrue(result.isSuccess)
        verify { userRepository.existsById(userId) }
        verify { userRepository.findById(userId) }
        verify { userRepository.save(any()) }

        Assertions.assertNotNull(deletedUserSlot.captured.deletedAt)
    }

    @Test
    fun `should fail when user does not exist`() {
        val userId = UUID.randomUUID()

        every { userRepository.existsById(userId) } returns false

        val result = service.execute(userId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is NoSuchElementException)
        Assertions.assertEquals("User not found", result.exceptionOrNull()?.message)
        verify { userRepository.existsById(userId) }
        verify(exactly = 0) { userRepository.findById(any()) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should propagate repository error`() {
        val userId = UUID.randomUUID()
        val error = RuntimeException("Database error")

        every { userRepository.existsById(userId) } throws error

        val result = service.execute(userId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { userRepository.existsById(userId) }
    }
}
