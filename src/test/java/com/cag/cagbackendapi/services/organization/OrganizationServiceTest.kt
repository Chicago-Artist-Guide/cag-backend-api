package com.cag.cagbackendapi.services.organization

import com.cag.cagbackendapi.services.organization.impl.OrganizationService
import com.nhaarman.mockito_kotlin.*
import org.mockito.Mock
import org.mockito.InjectMocks
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger

@ExtendWith(MockitoExtension::class)
internal class OrganizationServiceTest {

    @Mock
    private lateinit var logger: Logger

    @InjectMocks
    private lateinit var organizationService: OrganizationService

    @Test
    fun registerOrganization_validInput_logsAndSucceeds() {
        // assemble
        val inputMessage = "some org"
        val testMessage = "Test organization service: $inputMessage"

        doNothing().whenever(logger).info(testMessage)

        // act
        organizationService.testOrganizationService(inputMessage)

        // assert
        verify(logger).info(testMessage)
        verifyNoMoreInteractions(logger)
    }
}
