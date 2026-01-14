package com.example.application.user.service

import com.example.application.user.model.UserModel
import com.example.application.user.repository.UserRepository
import com.example.domain.user.entity.dto.StoreUserDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

class StoreUserServiceTest {
    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()

    private val service = StoreUserService(userRepository, passwordEncoder)

    @Test
    fun `should store user successfully with encoded password`() {
        val storeUserDto = StoreUserDto(
            name = "John Doe",
            email = "john@example.com",
            password = "plainPassword123"
        )

        val encodedPassword = "encodedPassword123"
        val savedUserModel = UserModel(
            id = UUID.randomUUID(),
            name = storeUserDto.name,
            email = storeUserDto.email,
            password = encodedPassword
        )

        val userModelSlot = slot<UserModel>()

        every { passwordEncoder.encode(storeUserDto.password) } returns encodedPassword
        every { userRepository.save(capture(userModelSlot)) } returns savedUserModel

        val result = service.execute(storeUserDto)

        Assertions.assertTrue(result.isSuccess)
        val user = result.getOrNull()
        Assertions.assertNotNull(user)
        Assertions.assertEquals(savedUserModel.id, user?.id)
        Assertions.assertEquals(storeUserDto.name, user?.name)
        Assertions.assertEquals(storeUserDto.email, user?.email)
        Assertions.assertEquals(encodedPassword, user?.password)

        verify { passwordEncoder.encode(storeUserDto.password) }
        verify { userRepository.save(any()) }

        Assertions.assertEquals(storeUserDto.name, userModelSlot.captured.name)
        Assertions.assertEquals(storeUserDto.email, userModelSlot.captured.email)
        Assertions.assertEquals(encodedPassword, userModelSlot.captured.password)
    }

    @Test
    fun `should propagate repository error`() {
        val storeUserDto = StoreUserDto(
            name = "John Doe",
            email = "john@example.com",
            password = "plainPassword123"
        )

        val encodedPassword = "encodedPassword123"
        val error = RuntimeException("Database error")

        every { passwordEncoder.encode(storeUserDto.password) } returns encodedPassword
        every { userRepository.save(any()) } throws error

        val result = service.execute(storeUserDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { passwordEncoder.encode(storeUserDto.password) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should propagate password encoder error`() {
        val storeUserDto = StoreUserDto(
            name = "John Doe",
            email = "john@example.com",
            password = "plainPassword123"
        )

        val error = RuntimeException("Encoder error")

        every { passwordEncoder.encode(storeUserDto.password) } throws error

        val result = service.execute(storeUserDto)

        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(error, result.exceptionOrNull())
        verify { passwordEncoder.encode(storeUserDto.password) }
        verify(exactly = 0) { userRepository.save(any()) }
    }
}
