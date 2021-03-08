package com.cag.cagbackendapi.services.user

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.daos.impl.UserDao
import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.dtos.UserResponseDto
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.InternalServerErrorException
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
import com.cag.cagbackendapi.services.user.impl.UserService
import com.nhaarman.mockitokotlin2.*
import org.hibernate.annotations.NotFound
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    private var userDao: UserDao = mock()

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun registerUser_validUser_logsAndSucceeds() {
        // assemble
        val inputUser = RegisterUserRequestDto("testy", "tester", "testytester@aol.com")
        val resultUser = UserResponseDto(UUID.randomUUID(), "testy", "tester", "testytester@aol.com", true, null)

        whenever(userDao.saveUser(inputUser)).thenReturn(resultUser)

        // act
        userService.registerUser(inputUser)

        // assert
        verify(userDao).saveUser(inputUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun registerUser_missingFirstNameAndEmail_BadRequest() {
        // assemble
        val inputUser = RegisterUserRequestDto(null, null,null)
        val badRequestException = BadRequestException(DetailedErrorMessages.NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED, null)

        // act
        val actualException = assertThrows<BadRequestException> {
            userService.registerUser(inputUser)
        }

        assertEquals(badRequestException.message, actualException.message)

        // assert
        verifyZeroInteractions(userDao)
    }

    @Test
    fun registerUser_validInputWithDatabaseDown_InternalServerError() {
        // assemble
        val inputUser = RegisterUserRequestDto("test", "user", "testuser@aol.com")
        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(userDao.saveUser(inputUser)).thenThrow(internalServerError)

        // act
        val actualException = assertThrows<InternalServerErrorException> {
            userService.registerUser(inputUser)
        }

        assertEquals(actualException.message, internalServerError.message)

        // assert
        verify(userDao).saveUser(inputUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun getByUserId_validUser_success() {
        val userId = UUID.randomUUID()

        val result = UserResponseDto(userId, "Test", "User", "test.user@gmail.com")

        whenever(userDao.getUser(userId)).thenReturn(result)

        userService.getByUserId(userId.toString())

        verify(userDao).getUser(userId)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun getByUserId_invalidId_badRequest() {
        val userId = "sadUserId"

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        val actualException = assertThrows<BadRequestException> {
            userService.getByUserId(userId)
        }

        assertEquals(badRequestException.message, actualException.message)

        verifyZeroInteractions(userDao)
    }

    @Test
    fun getByUserId_nullId_badRequest() {
        val userId = ""

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        val actualException = assertThrows<BadRequestException> {
            userService.getByUserId(userId)
        }

        assertEquals(badRequestException.message, actualException.message)

        verifyZeroInteractions(userDao)
    }

    @Test
    fun getByUserId_userNotFound_notFoundRequest() {
        val userId = UUID.randomUUID()

        val notFoundException = NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        whenever(userDao.getUser(userId)).thenReturn(null)

        val actualException = assertThrows<NotFoundException> {
            userService.getByUserId(userId.toString())
        }

        assertEquals(notFoundException.message, actualException.message)

        verify(userDao).getUser(userId)
        verifyZeroInteractions(userDao)
    }

    @Test
    fun getByUserId_validInputWithDatabaseDown_InternalServerError() {
        val userId = UUID.randomUUID()

        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(userDao.getUser(userId)).thenThrow(internalServerError)

        val actualException = assertThrows<InternalServerErrorException> {
            userService.getByUserId(userId.toString())
        }

        assertEquals(actualException.message, internalServerError.message)

        verify(userDao).getUser(userId)
        verifyNoMoreInteractions(userDao)
    }
}