package com.cag.cagbackendapi.integration

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.errors.ErrorDetails
import com.cag.cagbackendapi.util.SpringCommandLineProfileResolver
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import java.util.*

// NOTE: Update active profile to reflect your operating system to connect to database
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["LOCAL"], resolver = SpringCommandLineProfileResolver::class)
class UserControllerIntegrationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private val objectMapper = jacksonObjectMapper()

    private val validRegisterUser = UserRegistrationDto("first name", "last name", "user", true)
    private val validAuthKey = "mockAuthKey"

    @Test
    fun registerUser_validInput_201Success() {
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)

        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertEquals(validRegisterUser.first_name, createUser.first_name)
        assertEquals(validRegisterUser.last_name, createUser.last_name)
        assertEquals(validRegisterUser.email, createUser.email)
        assertNotNull(createUser.user_id)
    }

    @Test
    fun registerUser_emptyFirstNameAndLastNameAndNot18_400BadRequest() {
        val emptyNameUser = UserDto(null, "", "", "testuser@aol.com", true, null, null, null)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(emptyNameUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.MUST_BE_18)
    }

    @Test
    fun registerUser_nullName_400BadRequest() {
        val nullNameUser = UserDto(null, null, null,"testuser@aol.com", true, null, null, true)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(nullNameUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED)
    }

    @Test
    fun registerUser_emptyEmail_400BadRequest() {
        val emptyEmailUser = UserDto(null, "test", "user", "", true, null, null, true)

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
        val nullEmailUser = UserDto(null, "test", "user", null, true, null, null, true)

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
    fun registerUser_nullEmailAndFirstNameAndLastName_400BadRequest() {
        val nullEmailUser = UserDto(null, null, null, null, true, null, null, true)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(nullEmailUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED)
    }

    @Test
    fun registerUser_badAuthKey_401Unauthorized() {
        val headers = HttpHeaders()
        headers.set("authKey", "wrongAuthKey")
        val request = HttpEntity(validRegisterUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.UNAUTHORIZED_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.WRONG_AUTH_KEY)
    }

    @Test
    fun updateUser_validInput_200Success() {
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userId = createUser.user_id

        val validUpdateUser = UserDto(user_id = userId, first_name = "Tony", last_name = "Stark", email="tstark@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(validUpdateUser, headers2)

        val updateUserResponse = testRestTemplate.exchange("/user/${validUpdateUser.user_id.toString()}", HttpMethod.PUT, request2, String::class.java)
        val updatedUser = objectMapper.readValue(updateUserResponse.body, UserDto::class.java)

        //test created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertEquals(validRegisterUser.first_name, createUser.first_name)
        assertEquals(validRegisterUser.email, createUser.email)
        assertNotNull(createUser.user_id)

        //test updated user
        assertNotNull(updateUserResponse)
        assertEquals(HttpStatus.OK, updateUserResponse.statusCode)
        assertEquals(createUser.user_id, updatedUser.user_id)
        assertEquals(validUpdateUser.first_name, updatedUser.first_name)
        assertEquals(validUpdateUser.last_name, updatedUser.last_name)
        assertEquals(validUpdateUser.email, updatedUser.email)
        assertNotNull(validUpdateUser.user_id)
    }

    @Test
    fun updateUser_invalidUserId_400BadRequest() {
        val userId = "blah"
        val invalidUpdateUser = UserDto(null, first_name = "Tony", last_name = "Stark", email="tstark@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(invalidUpdateUser, headers2)

        val errorDetailsResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.PUT, request2, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.INVALID_USER_ID)
    }

    @Test
    fun updateUser_missingUserId_404NotFound() {
        val invalidUpdateUser = UserDto(null, first_name = "Tony", last_name = "Stark", email="tstark@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(invalidUpdateUser, headers2)

        val errorDetailsResponse = testRestTemplate.exchange("/user/", HttpMethod.PUT, request2, ErrorDetails::class.java)

        assertEquals(HttpStatus.NOT_FOUND, errorDetailsResponse.statusCode)
        //TODO add to this test once we have a better resource not found page
    }

    @Test
    fun updateUser_invalidAuthKey_401Unauthorized() {
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userId = createUser.user_id

        val validUpdateUser = UserDto(user_id = userId, first_name = "Tony", last_name = "Stark", email="tstark@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val headers2 = HttpHeaders()
        headers2.set("authKey", "invalidAuthKey")
        val request2 = HttpEntity(validUpdateUser, headers2)

        val errorDetailsResponse = testRestTemplate.exchange("/user/${userId.toString()}", HttpMethod.PUT, request2, ErrorDetails::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.UNAUTHORIZED_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.WRONG_AUTH_KEY)
    }

    @Test
    fun updateUser_userNotFound_404NotFound(){
        val validUpdateUser = UserDto(user_id = UUID.randomUUID(), first_name = "Tony", last_name = "Stark", email="tstark@gmail.com", active_status = true, session_id = null, img_url = null, agreed_18 = true)
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(validUpdateUser, headers2)

        val errorDetailsResponse = testRestTemplate.exchange("/user/${validUpdateUser.user_id.toString()}", HttpMethod.PUT, request2, ErrorDetails::class.java)

        assertEquals(HttpStatus.NOT_FOUND, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.NOT_FOUND_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.USER_NOT_FOUND)
    }

    @Test
    fun getUser_validInput_200Success() {
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userId = createUser.user_id

        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(null,headers2)

        val getUserResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.GET, request2, String::class.java)
        val getUser = objectMapper.readValue(getUserResponse.body, UserDto::class.java)

        assertNotNull(getUserResponse)
        assertEquals(HttpStatus.OK, getUserResponse.statusCode)
        assertEquals(validRegisterUser.first_name, getUser.first_name)
        assertEquals(validRegisterUser.email, getUser.email)
        assertNotNull(getUser.user_id)
    }

    @Test
    fun deleteUser_validInput_200Success(){
        //register User
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userId = createUser.user_id

        //delete user from database
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(null,headers2)

        val deletedUserResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.DELETE, request2, String::class.java)
        val deletedUser = objectMapper.readValue(deletedUserResponse.body, UserDto::class.java)

        assertNotNull(deletedUserResponse)
        assertEquals(HttpStatus.OK, deletedUserResponse.statusCode)
        assertEquals(validRegisterUser.first_name, deletedUser.first_name)
        assertEquals(validRegisterUser.email, deletedUser.email)
        assertNotNull(deletedUser.user_id)
    }

    @Test
    fun deleteUser_missingUserId_400BadRequest() {
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(null,headers)

        val userId = null
        val errorDetailsResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.DELETE, request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.INVALID_USER_ID)
    }

    @Test
    fun deleteUser_missingAuthKey_401Unauthorized(){
        val headers = HttpHeaders()
        headers.set("authKey", "invalidAuthKey")
        val request = HttpEntity(null,headers)

        val userId = UUID.randomUUID()
        val errorDetailsResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.DELETE, request, ErrorDetails::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.UNAUTHORIZED_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.WRONG_AUTH_KEY)
    }

    @Test
    fun deleteUser_userNotFound_404NotFound(){
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(null,headers)

        val userId= UUID.randomUUID()
        val errorDetailsResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.DELETE, request, ErrorDetails::class.java)

        assertEquals(HttpStatus.NOT_FOUND, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.NOT_FOUND_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.USER_NOT_FOUND)
    }
}