package com.cag.cagbackendapi.daos

import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import java.util.*

interface ProfileDaoI {
    fun saveProfile(userId: UUID, profileRegistrationDto: ProfileRegistrationDto): ProfileDto

}