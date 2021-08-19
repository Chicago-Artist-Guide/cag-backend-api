package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.LoggerMessages.GET_PROFILE
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_PROFILE
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_SKILL_MEMBER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_UNION_STATUS_MEMBER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_ETHNICITY_MEMBER
import com.cag.cagbackendapi.daos.ProfileDaoI
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.entities.*
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
import com.cag.cagbackendapi.repositories.*
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
    private lateinit var skillMemberRepository: SkillMemberRepository

    @Autowired
    private lateinit var ethnicityMemberRepository: EthnicityMemberRepository

    @Autowired
    private lateinit var unionStatusRepository: UnionStatusRepository

    @Autowired
    private lateinit var skillRepository: SkillRepository

    @Autowired
    private lateinit var ethnicityRepository: EthnicityRepository

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

        //check & save union status member entity to union status member table
        saveUnionStatusMemberEntity(savedProfileEntity, unionStatusEntity!!)

        //check for existing skill and create if not found
        if(profileRegistrationDto.actor_skills != null) {
            saveUserSkills(savedProfileEntity, profileRegistrationDto.actor_skills!!)
        }

        //check for existing skill and create if not found
        if(profileRegistrationDto.actor_ethnicity != null) {
            saveUserEthnicities(savedProfileEntity, profileRegistrationDto.actor_ethnicity!!)
        }

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

    override fun saveUserSkills(savedProfileEntity: ProfileEntity?, actorSkills: List<String>?) {
        if (actorSkills != null) {
            for (i in actorSkills){
                val skillEntity = getUserSkill(i.toLowerCase())

                val skillMemberEntity = SkillMemberEntity(
                        null,
                        savedProfileEntity,
                        skillEntity
                )
                skillMemberRepository.save(skillMemberEntity)
                logger.info(LOG_SAVE_SKILL_MEMBER(skillMemberEntity))
            }
        }
    }


    override fun saveUserEthnicities(savedProfileEntity: ProfileEntity?, actorEthnicity: List<String>?) {
        if (actorEthnicity != null) {
            for (i in actorEthnicity){
                val ethnicityEntity = getUserEthnicity(i.toLowerCase())

                val ethnicityMemberEntity = EthnicityMemberEntity(
                        null,
                        savedProfileEntity,
                        ethnicityEntity
                )
                ethnicityMemberRepository.save(ethnicityMemberEntity)
                logger.info(LOG_SAVE_ETHNICITY_MEMBER(ethnicityMemberEntity))
            }
        }
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

    private fun getUserSkill(userSkill: String?): SkillEntity {
        return if (skillRepository.getByName(userSkill) != null ) {
            skillRepository.getByName(userSkill)
        } else {
            val userSkillEntity = SkillEntity(null, userSkill)
            skillRepository.save(userSkillEntity)
        }
    }

    private fun getUserEthnicity(userEthnicity: String?): EthnicityEntity {
        return if (ethnicityRepository.getByName(userEthnicity) != null ) {
            ethnicityRepository.getByName(userEthnicity)
        } else {
            val userEthnicityEntity = EthnicityEntity(null, userEthnicity)
            ethnicityRepository.save(userEthnicityEntity)
        }
    }

    private fun clearBadRequestMsg() {
        badRequestMsg = ""
    }
}
