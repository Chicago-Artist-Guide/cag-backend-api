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

    private var badRequestMsg: String = ""

    private fun clearBadRequestMsg() {
        badRequestMsg = ""
    }

    override fun saveProfileExtraInfo(userId: UUID, profileRegistrationExtraInfoDto: ProfileRegistrationExtraInfoDto): ProfileExtraInfoDto {

        clearBadRequestMsg()

        if (badRequestMsg.isNotEmpty()) {
            throw BadRequestException(badRequestMsg, null)
        }

        val user = userRepository.getByUserId(userId) ?:
            throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        val userProfile = profileRepository.getByUserEntity_userId(userId) ?:
            throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        val profileExtraInfoDto = ProfileExtraInfoDto(
                profile_id = userProfile.profile_id,
                userEntity = user.toDto(),
        )

        return profileExtraInfoDto
        //get profile UUID using user UUID
        //save each entity using the registration dto &

    }
}


