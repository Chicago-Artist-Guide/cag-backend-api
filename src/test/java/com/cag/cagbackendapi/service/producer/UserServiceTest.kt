package com.cag.cagbackendapi.service.producer

import com.cag.cagbackendapi.model.UserModel
import com.cag.cagbackendapi.service.user.UserService
import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    private var logger: Logger = mock()

    @InjectMocks
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun registerUser_validUser_logsAndSucceeds() {
        // assemble
        val inputUser = UserModel("testy tester", "testytester@aol.com")
        val testMessage = "#### -> Producing message -> $inputUser"

        doNothing().whenever(logger).info(testMessage)

        // act
        userService.registerUser(inputUser)

        // assert
        verify(logger).info(testMessage)
        verifyNoMoreInteractions(logger)
    }

    @Test
    fun sendMessage_validButEmptyMessage_logsAndSucceeds() {
        // assemble
        val inputUser = UserModel("", "")
        val testMessage = "#### -> Producing message -> $inputUser"

        doNothing().whenever(logger).info(testMessage)

        // act
        userService.registerUser(inputUser)

        // assert
        verify(logger).info(testMessage)
        verifyNoMoreInteractions(logger)
    }

    @Test
    fun sendMessage_validNullMessage_logsAndSucceeds() {
        // assemble
        val inputMessage = null
        val testMessage = "#### -> Producing message -> $inputMessage"

        doNothing().whenever(logger).info(testMessage)

        // act
        userService.registerUser(inputMessage)

        // assert
        verify(logger).info(testMessage)
        verifyNoMoreInteractions(logger)
    }
}