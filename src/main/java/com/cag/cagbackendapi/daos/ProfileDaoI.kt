package com.cag.cagbackendapi.daos

import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.entities.ProfileEntity
import java.util.*

interface ProfileDaoI {
    fun saveProfile(userId: UUID, profileRegistrationDto: ProfileRegistrationDto): ProfileDto
    fun getUserWithProfile(userId: UUID): ProfileDto?
    fun getProfile(userId: UUID): ProfileDto?
    fun saveUserSkills(savedProfileEntity: ProfileEntity?, actorSkills: List<String>?)
    fun saveUserEthnicities(savedProfileEntity: ProfileEntity?, actorEthnicity: List<String>?)
}