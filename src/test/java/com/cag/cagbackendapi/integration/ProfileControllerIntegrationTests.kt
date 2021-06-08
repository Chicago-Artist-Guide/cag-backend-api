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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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

    private val validRegisterUser = UserRegistrationDto("first name", "last name", "user", "password", true, true)
    private val validRegisterProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio", landing_perform_type_on_stage = true, landing_perform_type_off_stage = false, actor_info_1_ethnicities = listOf("Hispanic"), actor_info_2_age_ranges = listOf(0,2), actor_info_2_gender_roles = listOf("male"), off_stage_roles_general = listOf("peter pan"), off_stage_roles_production = listOf("producer"),off_stage_roles_scenic = listOf("stage-hand"), off_stage_roles_lighting = listOf("light manger 1", "light manager 1"), off_stage_roles_hair_makeup_costumes = listOf("cosmetologist", "lead cosmetologist"), off_stage_roles_sound = listOf("sound tech 1"), profile_photo_url = "www.awsPhotoURL.com", demographic_union_status = "United Actors of America", demographic_websites = listOf("www.myPersonalProfile.com"))

    private val validAuthKey = "mockAuthKey"

    @Test
    fun registerProfile_validInput_201Success() {
        //create user headers
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        //create user
        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userIdUUID = createUser.userId

        //register profile headers
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        headers2.set("userId", userIdUUID.toString())
        val request2 = HttpEntity(validRegisterProfile, headers2)

        //create profile
        val userProfileResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request2, String::class.java)
        val createdProfile = objectMapper.readValue(userProfileResponse.body, ProfileDto::class.java)

        //check the created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertEquals(validRegisterUser.first_name, createUser.first_name)
        assertEquals(validRegisterUser.last_name, createUser.last_name)
        assertEquals(validRegisterUser.email, createUser.email)
        assertNotNull(createUser.userId)

        //check the created profile
        assertNotNull(userProfileResponse)
        assertEquals(HttpStatus.CREATED, userProfileResponse.statusCode)
        assertNotNull(createdProfile.profile_id)
        assertEquals(validRegisterProfile.pronouns, createdProfile.pronouns)
        assertEquals(validRegisterProfile.lgbtqplus_member, createdProfile.lgbtqplus_member)
        assertEquals(validRegisterProfile.gender_identity, createdProfile.gender_identity)
        assertEquals(validRegisterProfile.comfortable_playing_man, createdProfile.comfortable_playing_man)
        assertEquals(validRegisterProfile.comfortable_playing_women, createdProfile.comfortable_playing_women)
        assertEquals(validRegisterProfile.comfortable_playing_neither, createdProfile.comfortable_playing_neither)
        assertEquals(validRegisterProfile.comfortable_playing_transition, createdProfile.comfortable_playing_transition)
        assertEquals(validRegisterProfile.height_inches, createdProfile.height_inches)
        assertEquals(validRegisterProfile.agency, createdProfile.agency)
        assertEquals(validRegisterProfile.website_link_one, createdProfile.website_link_one)
        assertEquals(validRegisterProfile.website_link_two, createdProfile.website_link_two)
        assertEquals(validRegisterProfile.website_type_one, createdProfile.website_type_one)
        assertEquals(validRegisterProfile.website_type_two, createdProfile.website_type_two)
        assertEquals(validRegisterProfile.bio, createdProfile.bio)

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
        val userIdUUID = createUser.userId

        //register profile headers
        val headers2 = HttpHeaders()
        headers2.set("authKey", "invalidAuthKey")
        headers2.set("userId", userIdUUID.toString())
        val request2 = HttpEntity(validRegisterProfile, headers2)

        //create error message
        val errorDetailsResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request2, ErrorDetails::class.java)

        //check the created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertNotNull(createUser.userId)

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
        val request2 = HttpEntity(validRegisterProfile, headers2)

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
        val userIdUUID = createUser.userId

        //register profile headers
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        headers2.set("userId", userIdUUID.toString())
        val request2 = HttpEntity(validRegisterProfile, headers2)


        //create profile
        val userProfileResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request2, String::class.java)
        val createdProfile = objectMapper.readValue(userProfileResponse.body, ProfileDto::class.java)

        //register profile headers2
        val headers3 = HttpHeaders()
        headers3.set("authKey", validAuthKey)
        headers3.set("userId", userIdUUID.toString())
        val request3 = HttpEntity(validRegisterProfile, headers3)

        //Error create duplicate profile
        val errorDetailsResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request3, ErrorDetails::class.java)


        //check the created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertNotNull(createUser.userId)

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

    @Test
    fun getProfile_validInput_200Success() {
        //create user headers
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        //create user
        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userIdUUID = createUser.userId

        //register profile headers
        val headers2 = HttpHeaders()
        headers2.set("authKey", validAuthKey)
        val request2 = HttpEntity(validRegisterProfile, headers2)

        //create profile
        testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile/register", HttpMethod.POST, request2, String::class.java)

        //get profile headers
        val headers3 = HttpHeaders()
        headers3.set("authKey", validAuthKey)
        val request3 = HttpEntity(null, headers3)

        //get profile
        val getProfileResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile", HttpMethod.GET, request3, String::class.java)
        val getProfile = objectMapper.readValue(getProfileResponse.body, ProfileDto::class.java)

        //check the created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        assertEquals(validRegisterUser.first_name, createUser.first_name)
        assertEquals(validRegisterUser.last_name, createUser.last_name)
        assertEquals(validRegisterUser.email, createUser.email)
        assertNotNull(createUser.userId)

        //get the created profile
        assertNotNull(getProfileResponse)
        assertEquals(HttpStatus.OK, getProfileResponse.statusCode)
        assertNotNull(getProfile.profile_id)
        assertEquals(validRegisterProfile.pronouns, getProfile.pronouns)
        assertEquals(validRegisterProfile.lgbtqplus_member, getProfile.lgbtqplus_member)
        assertEquals(validRegisterProfile.gender_identity, getProfile.gender_identity)
        assertEquals(validRegisterProfile.comfortable_playing_man, getProfile.comfortable_playing_man)
        assertEquals(validRegisterProfile.comfortable_playing_women, getProfile.comfortable_playing_women)
        assertEquals(validRegisterProfile.comfortable_playing_neither, getProfile.comfortable_playing_neither)
        assertEquals(validRegisterProfile.comfortable_playing_transition, getProfile.comfortable_playing_transition)
        assertEquals(validRegisterProfile.height_inches, getProfile.height_inches)
        assertEquals(validRegisterProfile.agency, getProfile.agency)
        assertEquals(validRegisterProfile.website_link_one, getProfile.website_link_one)
        assertEquals(validRegisterProfile.website_link_two, getProfile.website_link_two)
        assertEquals(validRegisterProfile.website_type_one, getProfile.website_type_one)
        assertEquals(validRegisterProfile.website_type_two, getProfile.website_type_two)
        assertEquals(validRegisterProfile.bio, getProfile.bio)
    }

    @Test
    fun getProfile_invalidUserId_400BadRequest() {
        val userId = null

        //get profile headers
        val headers3 = HttpHeaders()
        headers3.set("authKey", validAuthKey)
        val request3 = HttpEntity(null, headers3)

        //create error message
        val errorDetailsResponse = testRestTemplate.exchange("/user/$userId/profile", HttpMethod.GET, request3, ErrorDetails::class.java)

        //check the error
        assertEquals(HttpStatus.BAD_REQUEST, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.BAD_REQUEST_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.INVALID_USER_ID)
    }

    @Test
    fun getProfile_noProfileForUser_404NotFound() {
        //create user headers
        val headers = HttpHeaders()
        headers.set("authKey", validAuthKey)
        val request = HttpEntity(validRegisterUser, headers)

        //create user
        val createdUserResponse = testRestTemplate.postForEntity("/user/register", request, String::class.java)
        val createUser = objectMapper.readValue(createdUserResponse.body, UserDto::class.java)
        val userIdUUID = createUser.userId


        //get profile headers
        val headers3 = HttpHeaders()
        headers3.set("authKey", validAuthKey)
        val request3 = HttpEntity(null, headers3)

        //create error message
        val errorDetailsResponse = testRestTemplate.exchange("/user/${userIdUUID.toString()}/profile", HttpMethod.GET, request3, ErrorDetails::class.java)

        //check the created user
        assertNotNull(createdUserResponse)
        assertEquals(HttpStatus.CREATED, createdUserResponse.statusCode)
        Assertions.assertEquals(validRegisterUser.first_name, createUser.first_name)
        Assertions.assertEquals(validRegisterUser.last_name, createUser.last_name)
        Assertions.assertEquals(validRegisterUser.email, createUser.email)
        assertNotNull(createUser.userId)

        //check the error
        assertEquals(HttpStatus.NOT_FOUND, errorDetailsResponse.statusCode)
        assertNotNull(errorDetailsResponse?.body?.time)
        assertEquals(errorDetailsResponse?.body?.restErrorMessage, RestErrorMessages.NOT_FOUND_MESSAGE)
        assertEquals(errorDetailsResponse?.body?.detailedMessage, DetailedErrorMessages.PROFILE_NOT_FOUND)
    }
}