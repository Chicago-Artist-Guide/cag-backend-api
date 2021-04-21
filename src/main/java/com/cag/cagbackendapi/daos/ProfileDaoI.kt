package com.cag.cagbackendapi.daos

import com.cag.cagbackendapi.dtos.ProfileDto

interface ProfileDaoI {
    fun saveProfile(profileDto: ProfileDto): ProfileDto
}