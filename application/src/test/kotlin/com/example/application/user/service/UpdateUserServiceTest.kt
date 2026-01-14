package com.example.application.user.service

import com.example.application.user.model.UserModel
import com.example.application.user.repository.UserRepository
import com.example.domain.user.entity.dto.UpdateUserDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

class UpdateUserServiceTest {
    private val userRepository: UserRepository = mockk()

    private val service = UpdateUserService(userRepository)

    @Test
    fun `should update user successfully`() {
        val userId = UUID.randomUUID()
        val updateUserDto = UpdateUserDto(
            id = userId,
            name = "John Updated",
            email = "john.updated@example.com",
            password = "newpassword"
        )

        val existingUserModel = UserModel(
            id = userId,
            name = "John Doe",
            email = "john@example.com",
            password = "oldpassword"
        )

        val updatedUserModel = existingUserModel.copy(
            name = updateUserDto.name,
            email = updateUserDto.email,
            password = updateUserDto.password
        )

        val userModelSlot = slot<UserModel>()

        every { userRepository.findById(userId) } returns Optional.of(existingUserModel)
        every { userRepository.save(capture(userModelSlot)) } returns updatedUserModel

        val result = service.execute(updateUserDto)

        Assertions.assertTrue(result.isSuccess)
        val user = result.getOrNull()
        Assertions.assertNotNull(user)
        Assertions.assertEquals(updateUserDto.id, user?.id)
        Assertions.assertEquals(updateUserDto.name, user?.name)
        Assertions.assertEquals(updateUserDto.email, user?.email)
        Assertions.assertEquals(updateUserDto.password, user?.password)

        verify { userRepository.findById(userId) }
        verify { userRepository.save(any()) }

        Assertions.assertEquals(updateUserDto.name, userModelSlot.captured.name)
        Assertions.assertEquals(updateUserDto.email, userModelSlot.captured.email)
        Assertions.assertEquals(updateUserDto.password, userModelSlot.captured.password)
    }

    @Test
    fun `should fail when user not found`() {
        val userId = UUID.randomUUID()
        val updateUserDto = UpdateUserDto(
            id = userId,
            name = "John Updated",
            email = "john.updated@example.com",
            password = "newpassword"
        )

        every { userRepository.findById(userId) } returns Optional.empty()

        val result = service.execute(updateUserDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertTrue(result.exceptionOrNull() is NoSuchElementException)
        Assertions.assertEquals("User not found", result.exceptionOrNull()?.message)
        verify { userRepository.findById(userId) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should propagate repository error`() {
        val userId = UUID.randomUUID()
        val updateUserDto = UpdateUserDto(
            id = userId,
            name = "John Updated",
            email = "john.updated@example.com",
            password = "newpassword"
        )

        val error = RuntimeException("Database error")

        every { userRepository.findById(userId) } throws error

        val result = service.execute(updateUserDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { userRepository.findById(userId) }
    }
}
