package com.cag.cagbackendapi.services.validation

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.InternalServerErrorException
import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger

@ExtendWith(MockitoExtension::class)
internal class ValidationServiceTest {

    @Mock
    private lateinit var logger: Logger

    @InjectMocks
    private lateinit var validationService: ValidationService

    @Test
    fun validateAuthKey_validAuthKey_succeeds() {
        // assemble
        val requestAuthKey = "key"

        // act
        validationService.validateAuthKey(requestAuthKey)

        // assert
        verifyZeroInteractions(logger)
    }

    @Test
    fun validateAuthKey_blankMessage_400BadRequest() {
        // assemble
        val inputMessage = ""

        doNothing().whenever(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)

        // act
        val actual = assertThrows<BadRequestException> {
            validationService.validateAuthKey(inputMessage)
        }

        // assert
        assertEquals(actual.message, DetailedErrorMessages.MISSING_AUTH_KEY)
        verify(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)
        verifyZeroInteractions(logger)
    }

    @Test
    fun validateAuthKey_nullMessage_400BadRequest() {
        // assemble
        val inputMessage = null

        doNothing().whenever(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)

        // act
        val actual = assertThrows<BadRequestException> {
            validationService.validateAuthKey(inputMessage)
        }

        // assert
        assertEquals(actual.message, DetailedErrorMessages.MISSING_AUTH_KEY)
        verify(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)
        verifyZeroInteractions(logger)
    }

    @Test
    fun validateAuthKey_messageWithMoney_400BadRequest() {
        // assemble
        val inputMessage = "null$"

        doNothing().whenever(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)

        // act
        val actual = assertThrows<InternalServerErrorException> {
            validationService.validateAuthKey(inputMessage)
        }

        // assert
        assertEquals(actual.message, DetailedErrorMessages.MISSING_AUTH_KEY)
        verify(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)
        verifyZeroInteractions(logger)
    }
}