package com.cag.cagbackendapi.integration

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.errors.ErrorDetails
import com.cag.cagbackendapi.dtos.UserResponseDto
import com.cag.cagbackendapi.util.SpringCommandLineProfileResolver
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles

// NOTE: Update active profile to reflect your operating system to connect to database
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["LOCAL"], resolver = SpringCommandLineProfileResolver::class)
class UserControllerIntegrationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private val objectMapper = jacksonObjectMapper()

    private val validTestUser = UserResponseDto(null, "test", "user", "testuser@aol.com")
    private val validAuthKey = "mockAuthKey"

    @Test
    fun registerUser_validInput_201Success() {
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validTestUser, headers)

        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserResponseDto::class.java)

        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertEquals(validTestUser.first_name, createUser.first_name)
        assertEquals(validTestUser.email, createUser.email)
        assertNotNull(createUser.user_id)
    }

    @Test
    fun registerUser_emptyFirstFirstName_400BadRequest() {
        val emptyNameUser = UserResponseDto(null, "", "", "testuser@aol.com")

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(emptyNameUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.FIRST_NAME_REQUIRED)
    }

    @Test
    fun registerUser_nullName_400BadRequest() {
        val nullNameUser = UserResponseDto(null, null, null,"testuser@aol.com")

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(nullNameUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.FIRST_NAME_REQUIRED)
    }

    @Test
    fun registerUser_emptyEmail_400BadRequest() {
        val emptyEmailUser = UserResponseDto(null, "test", "user", "")

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(emptyEmailUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.EMAIL_REQUIRED)
    }

    @Test
    fun registerUser_nullEmail_400BadRequest() {
        val nullEmailUser = UserResponseDto(null, "test", "user", null)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(nullEmailUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.EMAIL_REQUIRED)
    }

    @Test
    fun registerUser_nullEmailAndFirstName_400BadRequest() {
        val nullEmailUser = UserResponseDto(null, null, null, null)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(nullEmailUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED)
    }

    @Test
    fun registerUser_badAuthKey_401Unauthorized() {
        val headers = HttpHeaders()
        headers.set("authKey", "wrongAuthKey")
        val request = HttpEntity(validTestUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.UNAUTHORIZED_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.WRONG_AUTH_KEY)
    }
}
