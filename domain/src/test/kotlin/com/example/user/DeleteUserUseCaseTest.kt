package com.example.user

import com.example.domain.user.gateway.DeleteUserGateway
import com.example.domain.user.usecase.DeleteUserUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class DeleteUserUseCaseTest {
    private val deleteUserGateway: DeleteUserGateway = mockk()

    private val useCase = DeleteUserUseCase(deleteUserGateway)

    @Test
    fun `should delete user successfully`() {
        val userId = UUID.randomUUID()

        every { deleteUserGateway.execute(userId) } returns Result.success(Unit)

        val result = useCase.execute(userId)

        Assertions.assertTrue(result.isSuccess)
        verify { deleteUserGateway.execute(userId) }
    }

    @Test
    fun `should propagate gateway error`() {
        val userId = UUID.randomUUID()
        val error = RuntimeException("Delete user error")

        every { deleteUserGateway.execute(userId) } returns Result.failure(error)

        val result = useCase.execute(userId)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { deleteUserGateway.execute(userId) }
    }
}
