package com.cag.cagbackendapi.controllers

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.RestErrorMessages
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.ConflictException
import com.cag.cagbackendapi.errors.exceptions.InternalServerErrorException
import com.cag.cagbackendapi.errors.exceptions.UnauthorizedException
import com.cag.cagbackendapi.services.user.impl.ProfileService
import com.cag.cagbackendapi.services.validation.impl.ValidationService
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)

class ProfileControllerTest {

    private var profileService: ProfileService = mock()
    private var validationService: ValidationService = mock()

    @InjectMocks
    private lateinit var profileController: ProfileController

    @Test
    fun registerProfile_validInput_return201(){
        val testAuthKey = "testAuthKey"
        val userIdUUID = UUID.randomUUID()
        val userId = userIdUUID.toString()
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")
        val userProfileResponse = ProfileDto(userIdUUID, pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(profileService.registerProfile(userId, userProfile)).thenReturn(userProfileResponse)

        profileController.registerProfile(testAuthKey, userId, userProfile)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(profileService).registerProfile(userId, userProfile)
        verifyNoMoreInteractions(validationService, profileService)
    }

    @Test
    fun registerProfile_missingAuthKey_401UnauthorizedRequest(){
        val testAuthKey = ""
        val userIdUUID = UUID.randomUUID()
        val userId = userIdUUID.toString()
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        val unauthorizedException = UnauthorizedException(DetailedErrorMessages.MISSING_AUTH_KEY, null)

        whenever(validationService.validateAuthKey(testAuthKey)).thenThrow(unauthorizedException)

        val actual = assertThrows<UnauthorizedException> {
            profileController.registerProfile(testAuthKey, userId, userProfile)
        }

        Assertions.assertEquals(actual.message, unauthorizedException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verifyZeroInteractions(profileService)
   }


   @Test
   fun registerProfile_missingUserId_400BadRequest(){
       val testAuthKey = "mockAuthKey"
       val userId = null
       val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

       val badRequestException = BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null)

       doNothing().whenever(validationService).validateAuthKey(testAuthKey)
       whenever(profileService.registerProfile(userId, userProfile)).thenThrow(badRequestException)

       val actual = assertThrows<BadRequestException> {
           profileController.registerProfile(testAuthKey, userId, userProfile)
       }

       Assertions.assertEquals(actual.message, badRequestException.message)

       verify(validationService).validateAuthKey(testAuthKey)
       verify(profileService).registerProfile(userId, userProfile)
       verifyNoMoreInteractions(validationService, profileService)
   }

    @Test
    fun registerProfile_existingProfile_403ConflictRequest(){
        val testAuthKey = ""
        val userIdUUID = UUID.randomUUID()
        val userId = userIdUUID.toString()
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        val conflictException = ConflictException(DetailedErrorMessages.USER_HAS_PROFILE, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(profileService.registerProfile(userId, userProfile)).thenThrow(conflictException)

        val actual = assertThrows<ConflictException> {
            profileController.registerProfile(testAuthKey, userId, userProfile)
        }

        Assertions.assertEquals(actual.message, conflictException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(profileService).registerProfile(userId, userProfile)
        verifyNoMoreInteractions(validationService, profileService)
    }

    @Test
    fun getProfile_validInput_Okay200() {
        val testAuthKey = "testAuthKey"
        val userUUID = UUID.randomUUID()
        val userId = userUUID.toString()
        val userProfileData = ProfileDto(userUUID, pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(profileService.getProfile(userId)).thenReturn(userProfileData)

        profileController.getProfile(testAuthKey, userId)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(profileService).getProfile(userId)
        verifyNoMoreInteractions(validationService, profileService)
    }

    @Test
    fun getProfile_invalidUserId_400BadRequest() {
        val testAuthKey = "testAuthKey"
        val userId = "-99"

        val badRequestException = BadRequestException(DetailedErrorMessages.USER_NOT_FOUND, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(profileService.getProfile(userId)).thenThrow(badRequestException)

        val actual = assertThrows<BadRequestException> {
            profileController.getProfile(testAuthKey, userId)
        }

        Assertions.assertEquals(actual.message, badRequestException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(profileService).getProfile(userId)
        verifyNoMoreInteractions(validationService, profileService)
    }

    @Test
    fun getProfile_invalidAuthKey_401UnAuthorized() {
        val testAuthKey = "testAuthKey"
        val userId = "test user"

        val unauthorizedException = UnauthorizedException(DetailedErrorMessages.MISSING_AUTH_KEY, null)

        whenever(validationService.validateAuthKey(testAuthKey)).thenThrow(unauthorizedException)

        val actual = assertThrows<UnauthorizedException> {
            profileController.getProfile(testAuthKey, userId)
        }

        Assertions.assertEquals(actual.message, unauthorizedException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verifyZeroInteractions(profileService)
    }

    @Test
    fun getByUserId_nonExistentUser_404NotFound() {
        val testAuthKey = "testAuthKey"
        val userId = null

        val badRequestException = BadRequestException(DetailedErrorMessages.USER_NOT_FOUND, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(profileService.getProfile(userId)).thenThrow(badRequestException)

        val actual = assertThrows<BadRequestException> {
            profileController.getProfile(testAuthKey, userId)
        }

        Assertions.assertEquals(actual.message, badRequestException.message)

        verify(validationService).validateAuthKey(testAuthKey)
        verify(profileService).getProfile(userId)
        verifyNoMoreInteractions(validationService, profileService)
    }

    @Test
    fun getProfile_InternalServerError500(){
        val testAuthKey = "testAuthKey"
        val userId = "valid UserID"
        val internalServerErrorException = InternalServerErrorException(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, null)

        doNothing().whenever(validationService).validateAuthKey(testAuthKey)
        whenever(profileService.getProfile(userId)).thenThrow(internalServerErrorException)

        val actual = assertThrows<InternalServerErrorException> {
            profileController.getProfile(testAuthKey, userId)
        }

        Assertions.assertEquals(actual.message, internalServerErrorException.message)
        verify(validationService).validateAuthKey(testAuthKey)
        verify(profileService).getProfile(userId)
        verifyNoMoreInteractions(validationService, profileService)

    }
}