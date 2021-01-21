package com.cag.cagbackendapi.services.validation

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.errors.exceptions.UnauthorizedException
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import java.lang.reflect.Field

@ExtendWith(MockitoExtension::class)
internal class ValidationServiceTest {

    @Mock
    private lateinit var logger: Logger

    @InjectMocks
    private lateinit var validationService: ValidationService

    @Test
    fun validateAuthKey_emptyAuthKey_401Unauthorized() {
        // assemble
        val inputMessage = ""

        doNothing().whenever(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)

        // act
        val actual = assertThrows<UnauthorizedException> {
            validationService.validateAuthKey(inputMessage)
        }

        // assert
        assertEquals(actual.message, DetailedErrorMessages.MISSING_AUTH_KEY)
        verify(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)
        verifyZeroInteractions(logger)
    }

    @Test
    fun validateAuthKey_nullAuthKey_401Unauthorized() {
        // assemble
        val inputMessage = null

        doNothing().whenever(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)

        // act
        val actual = assertThrows<UnauthorizedException> {
            validationService.validateAuthKey(inputMessage)
        }

        // assert
        assertEquals(actual.message, DetailedErrorMessages.MISSING_AUTH_KEY)
        verify(logger).error(DetailedErrorMessages.MISSING_AUTH_KEY)
        verifyZeroInteractions(logger)
    }

    @Test
    fun validateAuthKey_wrongAuthKey_401Unauthorized() {
        // assemble
        val inputMessage = "wrongAuthKey"

        doNothing().whenever(logger).error(DetailedErrorMessages.WRONG_AUTH_KEY)

        // act
        val actual = assertThrows<UnauthorizedException> {
            validationService.validateAuthKey(inputMessage)
        }

        // assert
        assertEquals(actual.message, DetailedErrorMessages.WRONG_AUTH_KEY)
        verify(logger).error(DetailedErrorMessages.WRONG_AUTH_KEY)
        verifyZeroInteractions(logger)
    }
}