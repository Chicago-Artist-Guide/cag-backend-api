package com.cag.cagbackendapi.controllers

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
import com.cag.cagbackendapi.errors.exceptions.UnauthorizedException
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
import com.cag.cagbackendapi.errors.exceptions.*
import com.cag.cagbackendapi.services.user.impl.UserService
import com.cag.cagbackendapi.services.validation.impl.ValidationService
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserControllerTest {

    private var userService: UserService = mock()
    private var validationService: ValidationService = mock()

    @InjectMocks
    private lateinit var userController: UserController

    @Test
    fun registerUser_validInput_returns201()   {
        val testAuthKey = "testAuthKey"
        val requestUser = UserRegistrationDto("John", "Smith", "johnjohn@aol.com", "password", true)
        val resultUser = UserDto(UUID.randomUUID(), "John", "Smith", "johnjohn@aol.com",  true, null, null, true)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.registerUser(requestUser)).thenReturn(resultUser)

        userController.registerUser(testAuthKey, requestUser)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).registerUser(requestUser)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun registerUser_missingFirstNameAndLastNameAndEmailAndPassword_400BadRequest() {
        val testAuthKey = "testAuthKey"
        val requestUser = UserRegistrationDto(first_name = null, last_name = null, email = null, pass = null, agreed_18 = true)

        val badRequestException = BadRequestException(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED + DetailedErrorMessages.PASSWORD_REQUIRED, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.registerUser(requestUser)).thenThrow(badRequestException)

        val actual = assertThrows<BadRequestException> {
            userController.registerUser(testAuthKey, requestUser)
        }

        assertEquals(actual.message, badRequestException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).registerUser(requestUser)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun registerUser_missingAuthKey_401UnauthorizedRequest() {
        val testAuthKey = ""
        val requestUser = UserRegistrationDto(first_name = "john", last_name = "smith", email = "jj@aol.com", pass = "password", agreed_18 = true)

        val unauthorizedException = UnauthorizedException(DetailedErrorMessages.MISSING_AUTH_KEY, null)

        whenever(validationService.validateAuthKey(testAuthKey)).thenThrow(unauthorizedException)

        val actual = assertThrows<UnauthorizedException> {
            userController.registerUser(testAuthKey, requestUser)
        }

        assertEquals(actual.message, unauthorizedException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verifyZeroInteractions(userService)
    }

    @Test
    fun updateUser_validInput_returns200(){
        val testAuthKey = "testAuthKey"
        val randomUUID = UUID.randomUUID()
        val randomUuidStr = randomUUID.toString()
        val updateUser = UserUpdateDto(first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com")
        val resultUpdateUser = UserDto(userId = randomUUID, first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.updateUser(randomUuidStr, updateUser)).thenReturn(resultUpdateUser)

        userController.updateUser(testAuthKey, updateUser, randomUuidStr)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).updateUser(randomUuidStr, updateUser)
        verifyNoMoreInteractions(validationService, userService)

    }

    @Test
    fun updateUser_missingUserId_returns400(){
        val testAuthKey = "testAuthKey"
        val updateUser = UserUpdateDto(first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com")

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_UUID, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.updateUser(null, updateUser)).thenThrow(badRequestException)

        val actual = assertThrows<BadRequestException> {
            userController.updateUser(testAuthKey, updateUser, null)
        }

        assertEquals(actual.message, badRequestException.message)
        verify(validationService).validateAuthKey(testAuthKey)
    }

    @Test
    fun updateUser_missingAuthKey_401UnauthorizedRequest(){
        val testAuthKey = ""
        val randomUUID = UUID.randomUUID()
        val randomUuidStr = randomUUID.toString()
        val updateUser = UserUpdateDto(first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com")

        val unauthorizedException = UnauthorizedException(DetailedErrorMessages.INVALID_UUID, null)

        whenever(validationService.validateAuthKey(testAuthKey)).thenThrow(unauthorizedException)

        val actual = assertThrows<UnauthorizedException> {
            userController.updateUser(testAuthKey, updateUser, randomUuidStr)
        }

        assertEquals(actual.message, unauthorizedException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verifyZeroInteractions(userService)

    }

    @Test
    fun updateUser_invalidUserId_returns404UserNotFound(){
        val testAuthKey = "testAuthKey"
        val randomUUID = UUID.randomUUID()
        val randomUuidStr = randomUUID.toString()
        val updateUser = UserUpdateDto(first_name = "DePaul", last_name = "sports", email="depaulSports@gmail.com")

        val notFoundException = NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.updateUser(randomUuidStr, updateUser)).thenThrow(notFoundException)

        val actual = assertThrows<NotFoundException> {
            userController.updateUser(testAuthKey, updateUser, randomUuidStr)
        }

        assertEquals(actual.message, notFoundException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).updateUser(randomUuidStr, updateUser)
        verifyNoMoreInteractions(validationService, userService)

    }

    @Test
    fun getByUserId_validInput_200OK() {
        val testAuthKey = "testAuthKey"
        val userId = "123e4567-e89b-12d3-a456-426614174000"
        val userUUID = UUID.fromString(userId)
        val userData = UserDto(userUUID, "John", "Smith", "johnjohn@aol.com", true, null, null, true)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.getByUserId(userId)).thenReturn(userData)

        userController.getByUserId(testAuthKey, userId)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).getByUserId(userId)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun getByUserId_missingUserId_400BadRequest() {
        val testAuthKey = "testAuthKey"
        val userId = "test user"

        val badRequestException = BadRequestException(DetailedErrorMessages.USER_NOT_FOUND, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.getByUserId(userId)).thenThrow(badRequestException)

        val actual = assertThrows<BadRequestException> {
            userController.getByUserId(testAuthKey, userId)
        }

        assertEquals(actual.message, badRequestException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).getByUserId(userId)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun getByUserId_invalidAuthKey_401UnAuthorized() {
        val testAuthKey = "testAuthKey"
        val userId = "test user"

        val unauthorizedException = UnauthorizedException(DetailedErrorMessages.MISSING_AUTH_KEY, null)

        whenever(validationService.validateAuthKey(testAuthKey)).thenThrow(unauthorizedException)

        val actual = assertThrows<UnauthorizedException> {
            userController.getByUserId(testAuthKey, userId)
        }

        assertEquals(actual.message, unauthorizedException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verifyZeroInteractions(userService)
    }

    @Test
    fun getByUserId_nonExistentUser_404NotFound() {
        val testAuthKey = "testAuthKey"
        val userId = null

        val badRequestException = BadRequestException(DetailedErrorMessages.USER_NOT_FOUND, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.getByUserId(userId)).thenThrow(badRequestException)

        val actual = assertThrows<BadRequestException> {
            userController.getByUserId(testAuthKey, userId)
        }

        assertEquals(actual.message, badRequestException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).getByUserId(userId)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun deleteUser_validInput_200OK() {
        val testAuthKey = "testAuthKey"
        val userId = "123e4567-e89b-12d3-a456-426614174000"
        val userUUID = UUID.fromString(userId)
        val userData = UserDto(userUUID, "John", "Smith", "johnjohn@aol.com", true, null, null, true)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.deleteUser(userId)).thenReturn(userData)

        userController.deleteUser(testAuthKey, userId)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).deleteUser(userId)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun deleteUser_missingUserId_400BadRequest(){
        val testAuthKey = "testAuthKey"
        val userId = "test user"

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.deleteUser(userId)).thenThrow(badRequestException)

        val actual = assertThrows<BadRequestException> {
            userController.deleteUser(testAuthKey, userId)
        }

        assertEquals(actual.message, badRequestException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).deleteUser(userId)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun deleteUser_invalidAuthKey_401Unauthorized(){
        val testAuthKey = "testAuthKey"
        val userId = "test user"

        val unauthorizedException = UnauthorizedException(DetailedErrorMessages.MISSING_AUTH_KEY, null)

        whenever(validationService.validateAuthKey(testAuthKey)).thenThrow(unauthorizedException)

        val actual = assertThrows<UnauthorizedException> {
            userController.deleteUser(testAuthKey, userId)
        }

        assertEquals(actual.message, unauthorizedException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verifyZeroInteractions(userService)
    }

    @Test
    fun deleteUser_invalidUserId_404UserNotFound(){
        val testAuthKey = "testAuthKey"
        val userId = "invalidUserId"

        val notFoundException = NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.deleteUser(userId)).thenThrow(notFoundException)

        val actual = assertThrows<NotFoundException> {
            userController.deleteUser(testAuthKey, userId)
        }

        assertEquals(actual.message, notFoundException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).deleteUser(userId)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun deleteUser_ServiceUnavailable503(){
        val testAuthKey = "testAuthKey"
        val userId = "valid UserID"
        val serviceUnavailableException = ServiceUnavailableException(RestErrorMessages.SERVICE_UNAVAILABLE_MESSAGE, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.deleteUser(userId)).thenThrow(serviceUnavailableException)

        val actual = assertThrows<ServiceUnavailableException> {
            userController.deleteUser(testAuthKey, userId)
        }

        assertEquals(actual.message, serviceUnavailableException.message)
        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).deleteUser(userId)
        verifyNoMoreInteractions(validationService, userService)

    }

    @Test
    fun deleteUser_InternalServerError500(){
        val testAuthKey = "testAuthKey"
        val userId = "valid UserID"
        val internalServerErrorException = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.deleteUser(userId)).thenThrow(internalServerErrorException)

        val actual = assertThrows<InternalServerErrorException> {
            userController.deleteUser(testAuthKey, userId)
        }

        assertEquals(actual.message, internalServerErrorException.message)
        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).deleteUser(userId)
        verifyNoMoreInteractions(validationService, userService)

    }
}
