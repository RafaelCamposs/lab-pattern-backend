package com.example.user

import com.example.domain.user.entity.User
import com.example.domain.user.entity.dto.UpdateUserDto
import com.example.domain.user.gateway.UpdateUserGateway
import com.example.domain.user.usecase.UpdateUserUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class UpdateUserUseCaseTest {
    private val updateUserGateway: UpdateUserGateway = mockk()

    private val useCase = UpdateUserUseCase(updateUserGateway)

    @Test
    fun `should update user successfully`() {
        val updateUserDto = UpdateUserDto(
            id = UUID.randomUUID(),
            name = "John Updated",
            email = "john.updated@example.com",
            password = "newpassword123"
        )

        val updatedUser = User(
            id = updateUserDto.id,
            name = updateUserDto.name,
            email = updateUserDto.email,
            password = updateUserDto.password
        )

        every { updateUserGateway.execute(updateUserDto) } returns Result.success(updatedUser)

        val result = useCase.execute(updateUserDto)

        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(updatedUser, result.getOrNull())
        verify { updateUserGateway.execute(updateUserDto) }
    }

    @Test
    fun `should propagate gateway error`() {
        val updateUserDto = UpdateUserDto(
            id = UUID.randomUUID(),
            name = "John Updated",
            email = "john.updated@example.com",
            password = "newpassword123"
        )

        val error = RuntimeException("Update user error")

        every { updateUserGateway.execute(updateUserDto) } returns Result.failure(error)

        val result = useCase.execute(updateUserDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { updateUserGateway.execute(updateUserDto) }
    }
}
