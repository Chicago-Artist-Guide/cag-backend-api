package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_PROFILE
import com.cag.cagbackendapi.daos.ProfileDaoI
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.entities.ProfileEntity
import com.cag.cagbackendapi.repositories.ProfileRepository
import com.cag.cagbackendapi.repositories.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileDao : ProfileDaoI{
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var logger: Logger

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun saveProfile(userId: UUID, profileRegistrationDto: ProfileRegistrationDto): ProfileDto {
        logger.info(LOG_SAVE_PROFILE(profileRegistrationDto))

        //try to move to service level (throw exception)
        val user = userRepository.getByUserId(userId) //?: throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND)

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
        return savedProfileEntity.toDto()
    }

    override fun getUserWithProfile(userId: UUID): ProfileDto? {
        return if(profileRepository.getByUserEntity_userId(userId).isEmpty()){
            null
        }else{
            profileRepository.getByUserEntity_userId(userId)[0].toDto()
        }
    }

    private fun profileDtoToEntity(profileDto: ProfileDto): ProfileEntity {
        return objectMapper.convertValue(profileDto, ProfileEntity::class.java) //.map(profileDto, ProfileEntity::class.java)
    }
}
