package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.LoggerMessages.GET_PROFILE
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_PROFILE
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_UNION_STATUS_MEMBER
import com.cag.cagbackendapi.daos.ProfileDaoI
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.entities.ProfileEntity
import com.cag.cagbackendapi.entities.UnionStatusEntity
import com.cag.cagbackendapi.entities.UnionStatusMemberEntity
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
import com.cag.cagbackendapi.repositories.ProfileRepository
import com.cag.cagbackendapi.repositories.UnionStatusMemberRepository
import com.cag.cagbackendapi.repositories.UnionStatusRepository
import com.cag.cagbackendapi.repositories.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileDao : ProfileDaoI {
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var unionStatusMemberRepository: UnionStatusMemberRepository

    @Autowired
    private lateinit var unionStatusRepository: UnionStatusRepository

    @Autowired
    private lateinit var logger: Logger

    private var badRequestMsg: String = ""

    override fun saveProfile(userId: UUID, profileRegistrationDto: ProfileRegistrationDto): ProfileDto {
        clearBadRequestMsg()
        logger.info(LOG_SAVE_PROFILE(profileRegistrationDto))

        val unionStatusEntity = validateUnionStatus(profileRegistrationDto.demographic_union_status)
        // validate ageIncrementEntity
        // validate ethnicityEntity
        // validate skillEntity

        if (badRequestMsg.isNotEmpty()) {
            throw BadRequestException(badRequestMsg, null)
        }

        val user = userRepository.getByUserId(userId) ?:
            throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        val profileEntity = ProfileEntity(
                null,
                profileRegistrationDto.pronouns,
                profileRegistrationDto.lgbtqplus_member,
                profileRegistrationDto.gender_identity,
                profileRegistrationDto.comfortable_playing_man,
                profileRegistrationDto.comfortable_playing_women,
                profileRegistrationDto.comfortable_playing_neither,
                profileRegistrationDto.comfortable_playing_transition,
                profileRegistrationDto.height_inches!!,
                profileRegistrationDto.agency,
                profileRegistrationDto.website_link_one,
                profileRegistrationDto.website_link_two,
                profileRegistrationDto.website_type_one,
                profileRegistrationDto.website_type_two,
                profileRegistrationDto.bio,
                user)

        val savedProfileEntity = profileRepository.save(profileEntity)

        //create & save union status member entity to union status member table
        saveUnionStatusMemberEntity(savedProfileEntity, unionStatusEntity!!)

        return savedProfileEntity.toDto()
    }

    override fun getUserWithProfile(userId: UUID): ProfileDto? {
        val profile = profileRepository.getByUserEntity_userId(userId)

        if (profile == null) {
            return null
        } else {
            return profile.toDto()
        }
    }

    override fun getProfile(userId: UUID): ProfileDto? {
        logger.info(GET_PROFILE(userId))

        val profileEntity = profileRepository.getByUserEntity_userId(userId) ?: return null
        return profileEntity.toDto()
    }

    private fun saveUnionStatusMemberEntity(savedProfileEntity: ProfileEntity, unionStatusEntity: UnionStatusEntity){
        val unionStatusMemberEntity = UnionStatusMemberEntity(
                null,
                savedProfileEntity,
                unionStatusEntity
        )

        logger.info(LOG_SAVE_UNION_STATUS_MEMBER(unionStatusMemberEntity))
        unionStatusMemberRepository.save(unionStatusMemberEntity)
    }

    private fun validateUnionStatus(unionStatusName: String?): UnionStatusEntity? {
        val unionStatusEntity = unionStatusRepository.getByName(unionStatusName)

        if (unionStatusEntity == null) {
            badRequestMsg += DetailedErrorMessages.UNION_STATUS_NOT_SUPPORTED
        }

        return unionStatusEntity
    }

    private fun clearBadRequestMsg() {
        badRequestMsg = ""
    }
}
