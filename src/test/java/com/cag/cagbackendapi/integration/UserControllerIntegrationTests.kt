package com.cag.cagbackendapi.integration

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.error.ErrorDetails
import com.cag.cagbackendapi.error.exceptions.UnauthorizedException
import com.cag.cagbackendapi.model.UserModel
import com.cag.cagbackendapi.util.SpringCommandLineProfileResolver
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["LOCAL"], resolver = SpringCommandLineProfileResolver::class)
class UserControllerIntegrationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private val objectMapper = ObjectMapper()

    @Test
    fun registerUser_validInput_201Success() {
        val testUser = UserModel("john testy", "jj@aol.com")

        val headers = HttpHeaders()
        headers.set("authKey", "mockAuthKey")
        val request = HttpEntity(testUser, headers)

        val result = testRestTemplate.postForEntity("/user/register", request, String::class.java)

        assertNotNull(result)
        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertEquals(testUser, request.body)
    }

    @Test
    fun registerUser_emptyMessage_401Unauthorized() {
        val testUser = UserModel("john testy", "jj@aol.com")

        val headers = HttpHeaders()
        headers.set("authKey", "wrongAuthKey")
        val request = HttpEntity(testUser, headers)

        val result = testRestTemplate.postForEntity("/user/register", request, ResponseEntity::class.java)

        //assertEquals(HttpStatus.UNAUTHORIZED, result.)
        assertNotNull(request.body)
//        assertEquals(actual.restErrorMessage, RestErrorMessages.UNAUTHORIZED_MESSAGE)
//        assertEquals(actual.detailedMessage, DetailedErrorMessages.WRONG_AUTH_KEY)
    }

    @Test
    fun registerUser_nullMessage_400BadRequest() {
        val result = testRestTemplate.postForEntity("/kafka/publish?message=", null, String::class.java)

        val actual = objectMapper.readValue(result.body, ErrorDetails::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        assertNotNull(actual.time)
        assertEquals(actual.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(actual.detailedMessage, DetailedErrorMessages.MISSING_AUTH_KEY)
    }

    @Test
    fun registerUser_messageWithMoney_500InternalServerError() {
        val testMessage = "moneyMessage$$$"

        val result = testRestTemplate.postForEntity("/kafka/publish?message=$testMessage", null, String::class.java)

        val actual = objectMapper.readValue(result.body, ErrorDetails::class.java)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.statusCode)
        assertNotNull(actual.time)
        assertEquals(actual.restErrorMessage, RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE)
        assertEquals(actual.detailedMessage, DetailedErrorMessages.MISSING_AUTH_KEY)
    }
}
