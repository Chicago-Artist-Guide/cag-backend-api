package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.LoggerMessages.GET_PROFILE
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_AGE_INCREMENT_MEMBER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_PROFILE
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_SKILL_MEMBER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_UNION_STATUS_MEMBER
import com.cag.cagbackendapi.daos.ProfileDaoI
import com.cag.cagbackendapi.daos.ProfileExtraInfoDaoI
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileExtraInfoDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationExtraInfoDto
import com.cag.cagbackendapi.entities.*
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
import com.cag.cagbackendapi.repositories.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileExtraInfoDao : ProfileExtraInfoDaoI {
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var logger: Logger

    override fun saveProfileExtraInfo(userId: UUID, profileRegistrationExtraInfoDto: ProfileRegistrationExtraInfoDto): ProfileExtraInfoDto {

        val user = userRepository.getByUserId(userId) ?:
            throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        val userProfile = profileRepository.getByUserEntity_userId(userId) ?:
            throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        /*
        1. check to see if past performance list is null
        2. create a method that iterates through the list of past performance
        3. for each pastPerformanceEntity, build the entity and save to database
        */

        val profileExtraInfoDto = ProfileExtraInfoDto(
                profile_id = userProfile.profile_id,
                userEntity = user.toDto(),
        )

        return profileExtraInfoDto
    }
}


