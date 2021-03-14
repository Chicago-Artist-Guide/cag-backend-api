package com.cag.cagbackendapi.services.user

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.daos.impl.UserDao
import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserResponseDto
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.InternalServerErrorException
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
import com.cag.cagbackendapi.errors.exceptions.ServiceUnavailableException
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
    fun registerUser_missingFirstNameAndLastNameAndEmail_BadRequest() {
        // assemble
        val inputUser = RegisterUserRequestDto(null, null,null)
        val badRequestException = BadRequestException(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED, null)

        // act
        val actualException = assertThrows<BadRequestException> {
            userService.registerUser(inputUser)
        }

        // assert
        assertEquals(badRequestException.message, actualException.message)
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

        // assert
        assertEquals(actualException.message, internalServerError.message)
        verify(userDao).saveUser(inputUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun getByUserId_validUser_success() {
        val userId = UUID.randomUUID()

        val result = UserResponseDto(userId, "Test", "User", "test.user@gmail.com", true, null)

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

    @Test
    fun updateUser_validUser_logsAndSucceeds() {
        //assemble
        val userId = UUID.randomUUID()
        val updateUser = UserDto(user_id = userId, first_name = "Captain", last_name = "America", email = "capamerica@gmail.com")
        val resultUser = UserResponseDto(user_id = userId, first_name = "Captain", last_name = "America", email = "capamerica@gmail.com", active_status = true, session_id = null)

        whenever(userDao.updateUser(updateUser)).thenReturn(resultUser)
        //act
        userService.updateUser(updateUser)

        //assert
        verify(userDao).updateUser(updateUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun updateUser_missingFirstNameAndLastnameAndEmail_BadRequest(){
        //assemble
        val userId = UUID.randomUUID()
        val updateUser = UserDto(user_id = userId, first_name = "", last_name = null, email = "")
        val badRequestException = BadRequestException(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED, null)

        //act
        val actualException = assertThrows<BadRequestException> {
            userService.updateUser(updateUser)
        }

        //assert
        assertEquals(badRequestException.message, actualException.message)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun updateUser_missingUserId_BadRequest(){
        //assemble
        val updateUser = UserDto(user_id = null, first_name = "Tony", last_name = "Stark", email = "tstark@gmail.com")
        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_UUID, null)

        //act
        val actualException = assertThrows<BadRequestException> {
            userService.updateUser(updateUser)
        }

        //assert
        assertEquals(badRequestException.message, actualException.message)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun updateUser_userNotFound_ExceptionRequest(){
        //assemble
        val userId = UUID.randomUUID()
        val updateUser = UserDto(user_id = userId, first_name = "Tony", last_name = "Stark", email = "tstark@gmail.com")
        val notFoundException = NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        whenever(userDao.updateUser(updateUser)).thenReturn(null)

        //act
        val actualException = assertThrows<NotFoundException> {
            userService.updateUser(updateUser)
        }

        //assert
        assertEquals(notFoundException.message, actualException.message)
        verify(userDao).updateUser(updateUser)
        verifyNoMoreInteractions(userDao)
    }


    @Test
    fun updateUser_validInputWithDatabaseDown_InternalServiceError(){
        // assemble
        val userId = UUID.randomUUID()
        val updateUser = UserDto(user_id = userId, first_name = "Tony", last_name = "Stark", email = "tstark@gmail.com")
        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(userDao.updateUser(updateUser)).thenThrow(internalServerError)

        // act
        val actualException = assertThrows<InternalServerErrorException> {
            userService.updateUser(updateUser)
        }

        // assert
        assertEquals(actualException.message, internalServerError.message)
        verify(userDao).updateUser(updateUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun deleteUser_validUser_logsAndSucceeds() {
        // assemble
        val userId = UUID.randomUUID()
        val inputUser = RegisterUserRequestDto("testy", "tester", "testytester@aol.com")
        val resultUser = UserResponseDto(userId, "testy", "tester", "testytester@aol.com", true, null)

        whenever(userDao.deleteUser(userId)).thenReturn(resultUser)

        // act
        userService.deleteUser(userId.toString())

        // assert
        verify(userDao).deleteUser(userId)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun deleteUser_invalidId_badRequest() {
        val userId = "invalidUserId"

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        val actualException = assertThrows<BadRequestException> {
            userService.deleteUser(userId)
        }

        assertEquals(badRequestException.message, actualException.message)
        verifyZeroInteractions(userDao)
    }

    @Test
    fun deleteUser_nullId_badRequest() {
        val userId = null

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        val actualException = assertThrows<BadRequestException> {
            userService.deleteUser(userId)
        }

        assertEquals(badRequestException.message, actualException.message)
        verifyZeroInteractions(userDao)
    }

    @Test
    fun deleteUser_userNotFound_notFoundRequest() {
        val userId = UUID.randomUUID()

        val notFoundException = NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        whenever(userDao.deleteUser(userId)).thenReturn(null)

        val actualException = assertThrows<NotFoundException> {
            userService.deleteUser(userId.toString())
        }

        assertEquals(notFoundException.message, actualException.message)
        verify(userDao).deleteUser(userId)
        verifyZeroInteractions(userDao)
    }

    @Test
    fun deleteUser_validInputWithDatabaseDown_InternalServerError() {
        val userId = UUID.randomUUID()

        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(userDao.deleteUser(userId)).thenThrow(internalServerError)

        val actualException = assertThrows<InternalServerErrorException> {
            userService.deleteUser(userId.toString())
        }

        assertEquals(actualException.message, internalServerError.message)
        verify(userDao).deleteUser(userId)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun deleteUser_validInputWithServiceUnavailable_ServiceUnavailableException() {
        val userId = UUID.randomUUID()

        val serviceUnavailableException = ServiceUnavailableException(RestErrorMessages.SERVICE_UNAVAILABLE_MESSAGE, null)

        whenever(userDao.deleteUser(userId)).thenThrow(serviceUnavailableException)

        val actualException = assertThrows<ServiceUnavailableException> {
            userService.deleteUser(userId.toString())
        }

        assertEquals(actualException.message, serviceUnavailableException.message)
        verify(userDao).deleteUser(userId)
        verifyNoMoreInteractions(userDao)
    }
}