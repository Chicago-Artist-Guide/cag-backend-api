package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.daos.ProfileDaoI
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.entities.ProfileEntity
import com.cag.cagbackendapi.entities.UserEntity
import com.cag.cagbackendapi.repositories.ProfileRepository
import com.cag.cagbackendapi.repositories.UserRepository
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProfileDao : ProfileDaoI{
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @Autowired
    private lateinit var logger: Logger

    @Autowired
    private lateinit var modelMapper: ModelMapper

    override fun saveProfile(profileDto: ProfileDto): ProfileDto {
        //logger.info(LOG_SAVE_PROFILE(profileDto))
        val savedProfileEntity = profileRepository.save(profileDtoToEntity(profileDto))
        return savedProfileEntity.toDto()
    }

    private fun profileDtoToEntity(profileDto: ProfileDto): ProfileEntity {
        return modelMapper.map(profileDto, ProfileEntity::class.java)

    }

}