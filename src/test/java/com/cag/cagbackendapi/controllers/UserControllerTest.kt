package com.cag.cagbackendapi.controllers

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.errors.exceptions.UnauthorizedException
import com.cag.cagbackendapi.dtos.UserResponseDto
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.services.user.impl.UserService
import com.cag.cagbackendapi.services.validation.impl.ValidationService
import com.nhaarman.mockitokotlin2.*
import lombok.extern.java.Log
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
        val requestUser = RegisterUserRequestDto("John", "Smith", "johnjohn@aol.com")
        val resultUser = UserResponseDto(UUID.randomUUID(), "John", "Smith", "johnjohn@aol.com", true, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.registerUser(requestUser)).thenReturn(resultUser)

        userController.registerUser(testAuthKey, requestUser)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).registerUser(requestUser)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun registerUser_missingFirstNameAndEmail_400BadRequest() {
        val testAuthKey = "testAuthKey"
        val requestUser = RegisterUserRequestDto(first_name = null, last_name = null, email = null)

        val badRequestException = BadRequestException(DetailedErrorMessages.NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED, null)

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
        val requestUser = RegisterUserRequestDto(first_name = "john", last_name = "smith", email = "jj@aol.com")

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
    fun getByUserId_validInput_200OK() {
        val testAuthKey = "testAuthKey"
        val userId = "123e4567-e89b-12d3-a456-426614174000"
        val userUUID = UUID.fromString(userId)
        val userData = UserResponseDto(userUUID, "John", "Smith", "johnjohn@aol.com", true, null)

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

}
