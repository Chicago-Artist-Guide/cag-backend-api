package com.cag.cagbackendapi.services.user

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.daos.impl.UserDao
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.InternalServerErrorException
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
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
        val inputUser = UserRegistrationDto("testy", "tester", "testytester@aol.com", true)
        val resultUser = UserDto(UUID.randomUUID(), "testy", "tester", "testytester@aol.com", true, null, null, true)

        whenever(userDao.saveUser(inputUser)).thenReturn(resultUser)

        // act
        userService.registerUser(inputUser)

        // assert
        verify(userDao).saveUser(inputUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun registerUser_missingFirstNameAndLastNameAndEmailAndAgreed18_BadRequest() {
        // assemble
        val inputUser = UserRegistrationDto(null, null,null, false)
        val badRequestException = BadRequestException(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED + DetailedErrorMessages.MUST_BE_18, null)

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
        val inputUser = UserRegistrationDto("test", "user", "testuser@aol.com", true)
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

        val result = UserDto(userId, "Test", "User", "test.user@gmail.com", true, null, null, true)

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
        val userUuid = UUID.randomUUID()
        val userId = userUuid.toString()
        val updateUser = UserDto(user_id = userUuid, first_name = "Captain", last_name = "America", email = "capamerica@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val resultUser = UserDto(user_id = userUuid, first_name = "Captain", last_name = "America", email = "capamerica@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)

        whenever(userDao.updateUser(userUuid, updateUser)).thenReturn(resultUser)
        //act
        userService.updateUser(userId, updateUser)

        //assert
        verify(userDao).updateUser(userUuid, updateUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun updateUser_missingFirstNameAndLastnameAndEmail_BadRequest(){
        //assemble
        val userUuid = UUID.randomUUID()
        val userId = userUuid.toString()
        val updateUser = UserDto(user_id = userUuid, first_name = "", last_name = null, email = "", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val badRequestException = BadRequestException(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED, null)

        //act
        val actualException = assertThrows<BadRequestException> {
            userService.updateUser(userId, updateUser)
        }

        //assert
        assertEquals(badRequestException.message, actualException.message)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun updateUser_missingUserId_BadRequest(){
        //assemble
        val updateUser = UserDto(user_id = null, first_name = "Tony", last_name = "Stark", email = "tstark@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        //act
        val actualException = assertThrows<BadRequestException> {
            userService.updateUser(null, updateUser)
        }

        //assert
        assertEquals(badRequestException.message, actualException.message)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun updateUser_userNotFound_ExceptionRequest(){
        //assemble
        val userUuid = UUID.randomUUID()
        val userId = userUuid.toString()
        val updateUser = UserDto(user_id = userUuid, first_name = "Tony", last_name = "Stark", email = "tstark@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val notFoundException = NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        whenever(userDao.updateUser(userUuid, updateUser)).thenReturn(null)

        //act
        val actualException = assertThrows<NotFoundException> {
            userService.updateUser(userId, updateUser)
        }

        //assert
        assertEquals(notFoundException.message, actualException.message)
        verify(userDao).updateUser(userUuid, updateUser)
        verifyNoMoreInteractions(userDao)
    }


    @Test
    fun updateUser_validInputWithDatabaseDown_InternalServiceError(){
        // assemble
        val userUuid = UUID.randomUUID()
        val userId = userUuid.toString()
        val updateUser = UserDto(user_id = userUuid, first_name = "Tony", last_name = "Stark", email = "tstark@gmail.com", session_id = null, active_status = true, agreed_18 = true, img_url = null)
        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(userDao.updateUser(userUuid, updateUser)).thenThrow(internalServerError)

        // act
        val actualException = assertThrows<InternalServerErrorException> {
            userService.updateUser(userId, updateUser)
        }

        assertEquals(actualException.message, internalServerError.message)

        // assert
        verify(userDao).updateUser(userUuid, updateUser)
        verifyNoMoreInteractions(userDao)
    }
}
