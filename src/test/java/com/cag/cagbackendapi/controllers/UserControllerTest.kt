package com.cag.cagbackendapi.controllers

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.errors.exceptions.UnauthorizedException
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
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
    fun registerUser_validInput_returns201() {
        val testAuthKey = "testAuthKey"
        val testUser = UserDto(null, "John", "Smith", "johnjohn@aol.com")
        val resultUser = UserDto(UUID.randomUUID(), "John", "Smith", "johnjohn@aol.com")

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.registerUser(testUser)).thenReturn(resultUser)

        userController.registerUser(testAuthKey, testUser)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).registerUser(testUser)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun registerUser_missingFirstNameAndEmail_400BadRequest() {
        val testAuthKey = ""
        val testUser = UserDto(user_id = null, first_name = null, last_name = null, email = null)

        val badRequestException = BadRequestException(DetailedErrorMessages.NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(userService.registerUser(testUser)).thenThrow(badRequestException)

        val actual = assertThrows<BadRequestException> {
            userController.registerUser(testAuthKey, testUser)
        }

        assertEquals(actual.message, badRequestException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).registerUser(testUser)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun registerUser_missingAuthKey_401UnauthorizedRequest() {
        val testAuthKey = ""
        val testUser = UserDto(user_id = null, first_name = "john", last_name = "smith", email = "jj@aol.com")

        val unauthorizedException = UnauthorizedException(DetailedErrorMessages.MISSING_AUTH_KEY, null)

        whenever(validationService.validateAuthKey(testAuthKey)).thenThrow(unauthorizedException)

        val actual = assertThrows<UnauthorizedException> {
            userController.registerUser(testAuthKey, testUser)
        }

        assertEquals(actual.message, unauthorizedException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verifyZeroInteractions(userService)
    }
}
