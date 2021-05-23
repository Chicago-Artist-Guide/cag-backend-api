package com.cag.cagbackendapi.integration

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.errors.ErrorDetails
import com.cag.cagbackendapi.util.SpringCommandLineProfileResolver
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["LOCAL"], resolver = SpringCommandLineProfileResolver::class)

class ProfileControllerIntegrationTests {
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private val objectMapper = jacksonObjectMapper()

    private val validRegisterUser = UserRegistrationDto("first name", "last name", "user", "password", true)
    private val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

    private val validAuthKey = "mockAuthKey"

    @Test
    fun registerUser_validInput_201Success() {
        //create user headers
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        //create user
        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userIdUUID = createUser.user_id

        //register profile headers
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        headers2.set("userId", userIdUUID.toString())
        val request2 = HttpEntity(userProfile, headers2)

        //create profile
        val userProfileResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request2, String::class.java)
        val createdProfile = objectMapper.readValue(userProfileResponse.body, ProfileDto::class.java)

        //check the created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertNotNull(createUser.user_id)

        //check the created profile
        assertNotNull(userProfileResponse)
        assertEquals(HttpStatus.CREATED, userProfileResponse.statusCode)
        assertNotNull(createdProfile.profile_id)
        assertEquals(userProfile.pronouns, createdProfile.pronouns)
        assertEquals(userProfile.lgbtqplus_member, createdProfile.lgbtqplus_member)
        assertEquals(userProfile.gender_identity, createdProfile.gender_identity)
        assertEquals(userProfile.comfortable_playing_man, createdProfile.comfortable_playing_man)
        assertEquals(userProfile.comfortable_playing_women, createdProfile.comfortable_playing_women)
        assertEquals(userProfile.comfortable_playing_neither, createdProfile.comfortable_playing_neither)
        assertEquals(userProfile.comfortable_playing_transition, createdProfile.comfortable_playing_transition)
        assertEquals(userProfile.height_inches, createdProfile.height_inches)
        assertEquals(userProfile.agency, createdProfile.agency)
        assertEquals(userProfile.website_link_one, createdProfile.website_link_one)
        assertEquals(userProfile.website_link_two, createdProfile.website_link_two)
        assertEquals(userProfile.website_type_one, createdProfile.website_type_one)
        assertEquals(userProfile.website_type_two, createdProfile.website_type_two)
        assertEquals(userProfile.bio, createdProfile.bio)
    }

    @Test
    fun registerProfile_badAuthKey_401Unauthorized() {
        //create user headers
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        //create user
        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userIdUUID = createUser.user_id

        //register profile headers
        val headers2 = HttpHeaders()
        headers2.set("authKey", "invalidAuthKey")
        headers2.set("userId", userIdUUID.toString())
        val request2 = HttpEntity(userProfile, headers2)

        //create error message
        val errorDetailsResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request2, ErrorDetails::class.java)

        //check the created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertNotNull(createUser.user_id)

        //check the error
        assertEquals(HttpStatus.UNAUTHORIZED, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.UNAUTHORIZED_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.WRONG_AUTH_KEY)

    }

    @Test
    fun registerProfile_missingUserId_404NotFound(){

        //register profile headers
        val userId = null
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        headers2.set("userId", null) //is this needed?
        val request2 = HttpEntity(userProfile, headers2)

        //create error message
        val errorDetailsResponse = testRestTemplate.exchange("/user/$userId/profile/register", HttpMethod.POST, request2, ErrorDetails::class.java)

        //check the error
        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.INVALID_USER_ID)

    }

    @Test
    fun registerProfile_userHasExistingProfile_Conflict(){
        //create user headers
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        //create user
        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userIdUUID = createUser.user_id

        //register profile headers
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        headers2.set("userId", userIdUUID.toString())
        val request2 = HttpEntity(userProfile, headers2)

        //create profile
        val userProfileResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request2, String::class.java)
        val createdProfile = objectMapper.readValue(userProfileResponse.body, ProfileDto::class.java)

        //register profile headers2
        val headers3 = HttpHeaders()
        headers3.set("authKey", validAuthKey)
        headers3.set("userId", userIdUUID.toString())
        val request3 = HttpEntity(userProfile, headers3)

        //Error create duplicate profile
        val errorDetailsResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request3, ErrorDetails::class.java)


        //check the created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertNotNull(createUser.user_id)

        //check the created profile
        assertNotNull(userProfileResponse)
        assertEquals(HttpStatus.CREATED, userProfileResponse.statusCode)
        assertNotNull(createdProfile.profile_id)

        //check the error
        assertEquals(HttpStatus.CONFLICT, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.CONFLICT_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.USER_HAS_PROFILE)
    }
}