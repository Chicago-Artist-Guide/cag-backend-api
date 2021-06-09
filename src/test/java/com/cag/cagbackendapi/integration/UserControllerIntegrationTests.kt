package com.cag.cagbackendapi.integration

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
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

    private val validRegisterUser = UserRegistrationDto("first name", "last name", "user", "password", true, true)
    private val validAuthKey = "mockAuthKey"

    @Test
    fun registerUser_validInput_201Success() {
        val expectedActiveStatus = true

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
        assertEquals(expectedActiveStatus, createUser.active_status)
        assertEquals(validRegisterUser.agreed_privacy, createUser.agreed_privacy)
        assertEquals(validRegisterUser.agreed_18, createUser.agreed_18)
        assertNotNull(createUser.userId)
    }

    @Test
    fun registerUser_emptyFirstNameAndLastNameAndEmailAndPasswordAndNot18_400BadRequest() {
        val emptyNameUser = UserRegistrationDto("", "", "", "", false)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(emptyNameUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(RestErrorMessages.BAD_REQUEST_MESSAGE, errorDetailsResponse?.body?.restErrorMessage)
        assertEquals(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED + DetailedErrorMessages.PASSWORD_REQUIRED + DetailedErrorMessages.MUST_BE_18 + DetailedErrorMessages.MUST_AGREE_PRIVACY, errorDetailsResponse?.body?.detailedMessage)
    }

    @Test
    fun registerUser_nullFirstNameAndLastNameAndEmailAndPasswordAndNot18NoPrivacy_400BadRequest() {
        val emptyNameUser = UserRegistrationDto(null, null, null, null, false, false)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(emptyNameUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(RestErrorMessages.BAD_REQUEST_MESSAGE, errorDetailsResponse?.body?.restErrorMessage)
        assertEquals(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED + DetailedErrorMessages.PASSWORD_REQUIRED + DetailedErrorMessages.MUST_BE_18 + DetailedErrorMessages.MUST_AGREE_PRIVACY, errorDetailsResponse?.body?.detailedMessage)
    }

    @Test
    fun registerUser_nullName_400BadRequest() {
        val nullNameUser = UserRegistrationDto(null, null,"testuser@aol.com", "password", true, true)

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
        val emptyEmailUser = UserRegistrationDto("test", "user", "", "password", true, true)

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
        val nullEmailUser = UserRegistrationDto("test", "user", null, "password", true, true)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(nullEmailUser, headers)

        val errorDetailsResponse = testRestTemplate.postForEntity("/user/register", request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(RestErrorMessages.BAD_REQUEST_MESSAGE, errorDetailsResponse?.body?.restErrorMessage)
        assertEquals(DetailedErrorMessages.EMAIL_REQUIRED, errorDetailsResponse?.body?.detailedMessage)
    }

    @Test
    fun registerUser_nullEmailAndFirstNameAndLastNameAndPassword_400BadRequest() {
        val nullEmailUser = UserRegistrationDto(null, null, null, null, true, true)

        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(nullEmailUser, headers)

        val errorDetailsResponse = testRestTemplate.exchange("/user/register", HttpMethod.POST, request, ErrorDetails::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(RestErrorMessages.BAD_REQUEST_MESSAGE, errorDetailsResponse?.body?.restErrorMessage)
        assertEquals(DetailedErrorMessages.FIRST_NAME_REQUIRED + DetailedErrorMessages.LAST_NAME_REQUIRED + DetailedErrorMessages.EMAIL_REQUIRED + DetailedErrorMessages.PASSWORD_REQUIRED, errorDetailsResponse?.body?.detailedMessage)
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
        val userId = createUser.userId

        val validUpdateUser = UserUpdateDto(first_name = "Tony", last_name = "Stark", email="tstark@gmail.com")
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(validUpdateUser, headers2)

        val updateUserResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.PUT, request2, String::class.java)
        val updatedUser = objectMapper.readValue(updateUserResponse.body, UserDto::class.java)

        //test created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertEquals(validRegisterUser.first_name, createUser.first_name)
        assertEquals(validRegisterUser.email, createUser.email)
        assertNotNull(createUser.userId)

        //test updated user
        assertNotNull(updateUserResponse)
        assertEquals(HttpStatus.OK, updateUserResponse.statusCode)
        assertEquals(createUser.userId, updatedUser.userId)
        assertEquals(validUpdateUser.first_name, updatedUser.first_name)
        assertEquals(validUpdateUser.last_name, updatedUser.last_name)
        assertEquals(validUpdateUser.email, updatedUser.email)
        assertNotNull(userId)
    }

    @Test
    fun updateUser_invalidUserId_400BadRequest() {
        val userId = "blah"
        val invalidUpdateUser = UserUpdateDto(first_name = "Tony", last_name = "Stark", email="tstark@gmail.com")
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
        val nonExistingUserId = "ee62bb8e-3945-4a67-a898-afae826ba833"
        val invalidUpdateUser = UserUpdateDto(first_name = "Tony", last_name = "Stark", email="tstark@gmail.com")
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(invalidUpdateUser, headers2)

        val errorDetailsResponse = testRestTemplate.exchange("/user/$nonExistingUserId", HttpMethod.PUT, request2, ErrorDetails::class.java)

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
        val userId = createUser.userId

        val validUpdateUser = UserUpdateDto(first_name = "Tony", last_name = "Stark", email="tstark@gmail.com")
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
        val userId = UUID.randomUUID()
        val validUpdateUser = UserUpdateDto(first_name = "Tony", last_name = "Stark", email="tstark@gmail.com")
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(validUpdateUser, headers2)

        val errorDetailsResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.PUT, request2, ErrorDetails::class.java)

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
        val userId = createUser.userId

        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(null,headers2)

        val getUserResponse = testRestTemplate.exchange("/user/$userId", HttpMethod.GET, request2, String::class.java)
        val getUser = objectMapper.readValue(getUserResponse.body, UserDto::class.java)

        assertNotNull(getUserResponse)
        assertEquals(HttpStatus.OK, getUserResponse.statusCode)
        assertEquals(validRegisterUser.first_name, getUser.first_name)
        assertEquals(validRegisterUser.email, getUser.email)
        assertNotNull(getUser.userId)
    }

    @Test
    fun deleteUser_validInput_200Success(){
        //register User
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userId = createUser.userId

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
        assertNotNull(deletedUser.userId)
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