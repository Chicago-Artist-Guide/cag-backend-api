package com.cag.cagbackendapi.services.user

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.daos.impl.UserDao
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.InternalServerErrorException
import com.cag.cagbackendapi.services.user.impl.UserService
import com.nhaarman.mockitokotlin2.*
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
        val inputUser = UserDto(null, "testy", "tester", "testytester@aol.com")
        val resultUser = UserDto(UUID.randomUUID(), "testy", "tester", "testytester@aol.com")

        whenever(userDao.saveUser(inputUser)).thenReturn(resultUser)

        // act
        userService.registerUser(inputUser)

        // assert
        verify(userDao).saveUser(inputUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun registerUser_missingFirstNameAndEmail_400BadRequest() {
        // assemble
        val inputUser = UserDto(null, null, null,null)
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
    fun registerUser_validInput_500DatabaseIssue() {
        // assemble
        val inputUser = UserDto(null, "test", "user", "testuser@aol.com")
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
}