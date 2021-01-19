package com.cag.cagbackendapi.controllers

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.errors.exceptions.UnauthorizedException
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.services.user.UserService
import com.cag.cagbackendapi.services.validation.ValidationService
import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserControllerTest {

    private var userService: UserService = mock()
    private var validationService: ValidationService = mock()

    @InjectMocks
    private lateinit var userController: UserController

    @Test
    fun registerUser_validInput_returns201() {
        val testAuthKey = "testAuthKey"
        val testUser = UserDto(name = "John Smith", email = "johnjohn@aol.com")

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        doNothing().whenever(userService).registerUser(testUser)

        userController.registerUser(testUser, testAuthKey)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(userService).registerUser(testUser)
        verifyNoMoreInteractions(validationService, userService)
    }

    @Test
    fun registerUser_missingAuthKey_401UnauthorizedRequest() {
        val testAuthKey = ""
        val testUser = UserDto(name = "john smith", email = "jj@aol.com")

        val exception = UnauthorizedException(DetailedErrorMessages.MISSING_AUTH_KEY, null)

        whenever(validationService.validateAuthKey(testAuthKey)).thenThrow(exception)

        val actual = assertThrows<UnauthorizedException> {
            userController.registerUser(testUser, testAuthKey)
        }

        assertEquals(actual.message, exception.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verifyZeroInteractions(userService)
    }
}
