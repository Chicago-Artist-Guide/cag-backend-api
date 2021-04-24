package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_PROFILE
import com.cag.cagbackendapi.daos.ProfileDaoI
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.entities.ProfileEntity
import com.cag.cagbackendapi.entities.UserEntity
import com.cag.cagbackendapi.repositories.ProfileRepository
import com.cag.cagbackendapi.repositories.UserRepository
import javassist.NotFoundException
import org.modelmapper.ModelMapper
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
    private lateinit var modelMapper: ModelMapper

    override fun saveProfile(userId: UUID, profileRegistrationDto: ProfileRegistrationDto): ProfileDto {
        logger.info(LOG_SAVE_PROFILE(profileRegistrationDto))

        //try to move to service level (throw exception)
        val user = userRepository.getByUserId(userId) ?: throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND)

        val profileDto = ProfileDto(
                profile_id = null,
                pronouns = profileRegistrationDto.pronouns,
                lgbtqplus_member = profileRegistrationDto.lgbtqplus_member,
                gender_identity = profileRegistrationDto.gender_identity,
                comfortable_playing_man = profileRegistrationDto.comfortable_playing_man,
                comfortable_playing_women = profileRegistrationDto.comfortable_playing_women,
                comfortable_playing_neither = profileRegistrationDto.comfortable_playing_neither,
                comfortable_playing_transition = profileRegistrationDto.comfortable_playing_transition,
                height_inches = profileRegistrationDto.height_inches,
                agency = profileRegistrationDto.agency,
                website_link_one = profileRegistrationDto.website_link_one,
                website_link_two = profileRegistrationDto.website_link_two,
                website_type_one = profileRegistrationDto.website_type_one,
                website_type_two = profileRegistrationDto.website_type_two,
                bio = profileRegistrationDto.bio,
                user = user
        )

        val savedProfileEntity = profileRepository.save(profileDtoToEntity(profileDto))
        return savedProfileEntity.toDto()
    }

    /*override fun getUserWithProfile(userId: UUID): ProfileDto? {
        //return profileRepository.findByUserId(user_Id)[0].toDto()
        return profileRepository.findByUserId(userId)[0].toDto()
    }*/

    private fun profileDtoToEntity(profileDto: ProfileDto): ProfileEntity {
        return modelMapper.map(profileDto, ProfileEntity::class.java)

    }

}