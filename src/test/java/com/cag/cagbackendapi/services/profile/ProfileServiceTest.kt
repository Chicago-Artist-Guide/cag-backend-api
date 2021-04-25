package com.cag.cagbackendapi.services.profile

import com.cag.cagbackendapi.daos.impl.ProfileDao
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.services.user.impl.ProfileService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProfileServiceTest {

    private var profileDao: ProfileDao = mock()

    @InjectMocks
    private lateinit var profileService: ProfileService

    @Test
    fun registerProfile_Succeeds(){
        val userIdUUID = UUID.randomUUID()
        val userId = userIdUUID.toString()
        val userProfile = ProfileRegistrationDto(pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")
        val userProfileResponse = ProfileDto(userIdUUID, pronouns = "he/him", lgbtqplus_member = false, gender_identity = "", comfortable_playing_transition = true, comfortable_playing_man = true, comfortable_playing_women = true, comfortable_playing_neither = false, height_inches = 88, agency = "Pedro LLC", website_link_one = "", website_link_two = "", website_type_one = "", website_type_two = "", bio = "this is my bio")


        profileService.registerProfile(userId, userProfile)

        verify(profileDao).saveProfile(userIdUUID, userProfile)
        verifyNoMoreInteractions(profileDao)
    }
}