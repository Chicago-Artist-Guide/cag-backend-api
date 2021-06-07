package com.cag.cagbackendapi.services.profile

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.daos.impl.ProfileDao
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
import com.cag.cagbackendapi.errors.exceptions.*
import com.cag.cagbackendapi.services.user.impl.ProfileService
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProfileServiceTest {

    private var profileDao: ProfileDao = mock()
    private var passwordEncoder: PasswordEncoder = mock()

    @InjectMocks
    private lateinit var profileService: ProfileService

    @Test
    fun registerProfile_Succeeds(){
        val userIdUUID = UUID.randomUUID()
        val userId = userIdUUID.toString()
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        profileService.registerProfile(userId, userProfile)

        verify(profileDao).saveProfile(userIdUUID, userProfile)

    }

    @Test
    fun registerProfile_nullUserId_badRequest(){
        val userId = null
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        val actualException = assertThrows<BadRequestException> {
            profileService.registerProfile(userId, userProfile)
        }

        Assertions.assertEquals(badRequestException.message, actualException.message)

        verifyZeroInteractions(profileDao)
    }

    @Test
    fun registerProfile_invalidUserId_badRequest(){
        val userId = "invalidUserId"
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        val actualException = assertThrows<BadRequestException> {
            profileService.registerProfile(userId, userProfile)
        }

        Assertions.assertEquals(badRequestException.message, actualException.message)

        verifyZeroInteractions(profileDao)
    }

    @Test
    fun registerProfile_validInputWithDatabaseDown_InternalServerError() {
        val userIdUUID = UUID.randomUUID()
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(profileDao.saveProfile(userIdUUID, userProfile)).thenThrow(internalServerError)

        val actualException = assertThrows<InternalServerErrorException> {
            profileDao.saveProfile(userIdUUID, userProfile)
        }

        Assertions.assertEquals(actualException.message, internalServerError.message)

        verify(profileDao).saveProfile(userIdUUID, userProfile)
        verifyNoMoreInteractions(profileDao)
    }

    @Test
    fun registerProfile_validInputWithServiceUnavailable_ServiceUnavailableException() {
        val userIdUUID = UUID.randomUUID()
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        val serviceUnavailableException = ServiceUnavailableException(RestErrorMessages.SERVICE_UNAVAILABLE_MESSAGE, null)
        whenever(profileDao.saveProfile(userIdUUID, userProfile)).thenThrow(serviceUnavailableException)

        val actualException = assertThrows<ServiceUnavailableException> {
            profileDao.saveProfile(userIdUUID, userProfile)
        }

        Assertions.assertEquals(actualException.message, serviceUnavailableException.message)
        verify(profileDao).saveProfile(userIdUUID, userProfile)
        verifyNoMoreInteractions(profileDao)

    }

    @Test
    fun registerProfile_existingProfile_conflictRequest(){
        val userIdUUID = UUID.randomUUID()
        val userId = userIdUUID.toString()
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        val conflictException = ConflictException(DetailedErrorMessages.USER_HAS_PROFILE, null)
        whenever(profileDao.saveProfile(userIdUUID, userProfile)).thenThrow(conflictException)

        val actualException = assertThrows<ConflictException> {
            profileService.registerProfile(userId, userProfile)
        }

        Assertions.assertEquals(conflictException.message, actualException.message)
        verify(profileDao).saveProfile(userIdUUID, userProfile)
    }

    @Test
    fun registerProfile_validateProfileDto(){
        //assemble
        val userIdUUID = UUID.randomUUID()
        val userId = userIdUUID.toString()
        val userProfile = ProfileRegistrationDto(pronouns = null, lgbtqplus_member = null, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = null, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "")
        val badRequestException = BadRequestException(DetailedErrorMessages.PRONOUN_REQUIRED + DetailedErrorMessages.LGBTQPLUS_MEMBER_REQUIRED  + DetailedErrorMessages.GENDER_IDENTITY_REQUIRED + DetailedErrorMessages.HEIGHT_INCHES_REQUIRED + DetailedErrorMessages.BIO_REQUIRED,null)

        //act
        val actualException = assertThrows<BadRequestException> {
            profileService.registerProfile(userId, userProfile)
        }

        //assert
        Assertions.assertEquals(badRequestException.message, actualException.message)
        verify(profileDao).getUserWithProfile(userIdUUID)
        verifyNoMoreInteractions(profileDao)
    }

    @Test
    fun getProfile_validUser_success() {
        val userId = UUID.randomUUID()

        val result = ProfileDto(userId, "Test", true, "", true, true, false, true, 0, "", "", "", "", "", "", userEntity = null)

        whenever(profileDao.getProfile(userId)).thenReturn(result)

        profileService.getProfile(userId.toString())

        verify(profileDao).getProfile(userId)
        verifyNoMoreInteractions(profileDao, passwordEncoder)
    }

    @Test
    fun getProfile_invalidId_badRequest() {
        val userId = "sadUserId"

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        val actualException = assertThrows<BadRequestException> {
            profileService.getProfile(userId)
        }

        Assertions.assertEquals(badRequestException.message, actualException.message)
        verifyZeroInteractions(profileDao, passwordEncoder)
    }

    @Test
    fun getProfile_nullId_badRequest() {
        val userId = ""

        val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

        val actualException = assertThrows<BadRequestException> {
            profileService.getProfile(userId)
        }

        Assertions.assertEquals(badRequestException.message, actualException.message)

        verifyZeroInteractions(profileDao, passwordEncoder)
    }

    @Test
    fun getProfile_profileNotFound_notFoundRequest() {
        val userId = UUID.randomUUID()

        val notFoundException = NotFoundException(DetailedErrorMessages.PROFILE_NOT_FOUND, null)

        whenever(profileDao.getProfile(userId)).thenReturn(null)

        val actualException = assertThrows<NotFoundException> {
            profileService.getProfile(userId.toString())
        }

        Assertions.assertEquals(notFoundException.message, actualException.message)
        verify(profileDao).getProfile(userId)
        verifyNoMoreInteractions(profileDao, passwordEncoder)
    }

    @Test
    fun getByUserId_validInputWithDatabaseDown_InternalServerError() {
        val userId = UUID.randomUUID()

        val internalServerError = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        whenever(profileDao.getProfile(userId)).thenThrow(internalServerError)

        val actualException = assertThrows<InternalServerErrorException> {
            profileService.getProfile(userId.toString())
        }

        Assertions.assertEquals(actualException.message, internalServerError.message)

        verify(profileDao).getProfile(userId)
        verifyNoMoreInteractions(profileDao, passwordEncoder)
    }


}