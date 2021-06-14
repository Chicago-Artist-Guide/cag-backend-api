package com.cag.cagbackendapi.services.user

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.daos.impl.UserDao
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserLoginDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
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
        val inputPass = "password"
        val inputUser = UserRegistrationDto("testy", "tester", "testytester@aol.com", inputPass, true, true)
        val resultUser = UserDto(UUID.randomUUID(), "testy", "tester", "testytester@aol.com", true, null, null, true, true)

        whenever(userDao.saveUser(inputUser)).thenReturn(resultUser)

        // act
        userService.registerUser(inputUser)

        // assert
        verify(userDao).saveUser(inputUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun registerUser_missingFirstNameAndLastNameAndEmailAndPasswordAndAgreed18AndPrivacyAgreement_BadRequest() {
        // assemble
        val inputUser = UserRegistrationDto(null, null, null, null, false, false)
        val badRequestException = BadRequestException(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED + DetailedErrorMessages.PASSWORD_REQUIRED + DetailedErrorMessages.MUST_BE_18 + DetailedErrorMessages.MUST_AGREE_PRIVACY, null)

        // act
        val actualException = assertThrows<BadRequestException> {
            userService.registerUser(inputUser)
        }

        // assert
        assertEquals(badRequestException.message, actualException.message)
        verifyZeroInteractions(userDao)
    }

    @Test
    fun registerUser_emptyFirstNameAndLastNameAndEmailAndPassword_BadRequest() {
        // assemble
        val inputUser = UserRegistrationDto("", "", "", "", true, true)
        val badRequestException = BadRequestException(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED + DetailedErrorMessages.PASSWORD_REQUIRED, null)

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
        val inputPass = "password"
        val inputUser = UserRegistrationDto("test", "user", "testuser@aol.com", inputPass,true, true)
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

        val result = UserDto(userId, "Test", "User", "test.user@gmail.com", true, null, null, true, true)

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
        verifyNoMoreInteractions(userDao)
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
        val updateUser = UserUpdateDto(first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com")
        val resultUser = UserDto(userId = userUuid, first_name = "Captain", last_name = "America", email = "capamerica@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true, agreed_privacy = true)

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
        val updateUser = UserUpdateDto(first_name = "", last_name = null, email = "")
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
        val updateUser = UserUpdateDto(first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com")
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
        val updateUser = UserUpdateDto(first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com")
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
        val updateUser = UserUpdateDto(first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com")
        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(userDao.updateUser(userUuid, updateUser)).thenThrow(internalServerError)

        // act
        val actualException = assertThrows<InternalServerErrorException> {
            userService.updateUser(userId, updateUser)
        }

        // assert
        assertEquals(actualException.message, internalServerError.message)
        verify(userDao).updateUser(userUuid, updateUser)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun deleteUser_validUser_logsAndSucceeds() {
        // assemble
        val userId = UUID.randomUUID()
        val resultUser = UserDto(userId, "testy", "tester", "testytester@aol.com", true, null, null, true, true)

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

    @Test
    fun loginUser_validUser_200logsAndSucceeds() {
        // assemble
        val inputUserId = UUID.randomUUID()
        val inputPass = "password"
        val inputUser = UserLoginDto(inputUserId.toString(), inputPass)
        val resultUser = UserDto(inputUserId, "testy", "tester", "testytester@aol.com", true, null, null, true, true)

        whenever(userDao.loginAndGetUser(inputUserId, inputUser.pass!!)).thenReturn(resultUser)

        // act
        userService.loginUser(inputUser)

        // assert
        verify(userDao).loginAndGetUser(inputUserId, inputUser.pass!!)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun loginUser_invalidUserId_400BadRequest() {
        // assemble
        val inputUser = UserLoginDto(null, "password")
        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        // act
        val actualException = assertThrows<BadRequestException> {
            userService.loginUser(inputUser)
        }

        // assert
        assertEquals(badRequestException.message, actualException.message)
        verifyZeroInteractions(userDao)
    }

    @Test
    fun loginUser_missingPassword_400BadRequest() {
        // assemble
        val inputUser = UserLoginDto(UUID.randomUUID().toString(), "")
        val badRequestException = BadRequestException(DetailedErrorMessages.PASSWORD_REQUIRED, null)

        // act
        val actualException = assertThrows<BadRequestException> {
            userService.loginUser(inputUser)
        }

        // assert
        assertEquals(badRequestException.message, actualException.message)
        verifyZeroInteractions(userDao)
    }

    @Test
    fun loginUser_incorrectPassword_400BadRequest() {
        // assemble
        val userUUID = UUID.randomUUID()
        val wrongPass = "wrongPass"
        val inputUser = UserLoginDto(userUUID.toString(), wrongPass)
        val badRequestException = BadRequestException(DetailedErrorMessages.INCORRECT_PASSWORD, null)

        whenever(userDao.loginAndGetUser(userUUID, wrongPass)).thenThrow(badRequestException)

        // act
        val actualException = assertThrows<BadRequestException> {
            userService.loginUser(inputUser)
        }

        // assert
        assertEquals(badRequestException.message, actualException.message)

        verify(userDao).loginAndGetUser(userUUID, wrongPass)
        verifyNoMoreInteractions(userDao)
    }

    @Test
    fun loginUser_nonExistingUser_404NotFound() {
        // assemble
        val userUUID = UUID.randomUUID()
        val userPassword = "password"
        val inputUser = UserLoginDto(userUUID.toString(), userPassword)
        val notFoundException = NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        whenever(userDao.loginAndGetUser(userUUID, userPassword)).thenThrow(notFoundException)

        // act
        val actualException = assertThrows<NotFoundException> {
            userService.loginUser(inputUser)
        }

        // assert
        assertEquals(notFoundException.message, actualException.message)

        verify(userDao).loginAndGetUser(userUUID, userPassword)
        verifyZeroInteractions(userDao)
    }

    @Test
    fun loginUser_validInputWithDatabaseDown_InternalServerError() {
        // assemble
        val userUUID = UUID.randomUUID()
        val userPassword = "password"
        val inputUser = UserLoginDto(userUUID.toString(), userPassword)
        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(userDao.loginAndGetUser(userUUID, userPassword)).thenThrow(internalServerError)

        // act
        val actualException = assertThrows<InternalServerErrorException> {
            userService.loginUser(inputUser)
        }

        // assert
        assertEquals(actualException.message, internalServerError.message)
        verify(userDao).loginAndGetUser(userUUID, userPassword)
        verifyNoMoreInteractions(userDao)
    }
}
