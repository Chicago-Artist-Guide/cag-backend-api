package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.LoggerMessages
import com.cag.cagbackendapi.daos.ProfileExtraInfoDaoI
import com.cag.cagbackendapi.dtos.ProfileExtraInfoDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationExtraInfoDto
import com.cag.cagbackendapi.entities.*
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
import com.cag.cagbackendapi.repositories.*
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileExtraInfoDao : ProfileExtraInfoDaoI {
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @Autowired
    private lateinit var awardsRepository: AwardRepository

    @Autowired
    private lateinit var trainingRepository: TrainingRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var logger: Logger

    override fun saveProfileExtraInfo(userId: UUID, profileRegistrationExtraInfoDto: ProfileRegistrationExtraInfoDto): ProfileExtraInfoDto {

        var awardEntityList = listOf<AwardEntity>()

        var trainingEntityList = listOf<TrainingEntity>()

        val user = userRepository.getByUserId(userId) ?:
            throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        val userProfile = profileRepository.getByUserEntity_userId(userId) ?:
            throw NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null)

        if(profileRegistrationExtraInfoDto.awards != null) {
            awardEntityList = saveAwards(userProfile, profileRegistrationExtraInfoDto.awards!!)
        }
        /*
        1. check to see if past performance list is null
        2. create a method that iterates through the list of past performance
        3. for each pastPerformanceEntity, build the entity and save to database
        */

        if(profileRegistrationExtraInfoDto.training != null) {
            trainingEntityList = saveTraining(userProfile, profileRegistrationExtraInfoDto.training!!)
        }

        val profileExtraInfoDto = ProfileExtraInfoDto(
                profile_id = userProfile.profile_id,
                userEntity = user.toDto(),
                awards = awardEntityList,
                training = trainingEntityList
        )

        return profileExtraInfoDto
    }

    private fun saveAwards(savedProfileEntity: ProfileEntity?, awards: List<AwardRegistrationEntity>) : List<AwardEntity> {
        var awardEntityList = mutableListOf<AwardEntity>()

        for (i in awards){
            val awardEntity = AwardEntity(
                    null,
                    name = i.name,
                    year_received = i.year_received,
                    award_url = i.award_url,
                    description = i.description,
                    profileEntity = savedProfileEntity
            )
            awardsRepository.save(awardEntity)
            logger.info(LoggerMessages.LOG_SAVE_AWARD(awardEntity))
            awardEntityList.add(awardEntity)
        }
        return awardEntityList
    }

    private fun saveTraining(savedProfileEntity: ProfileEntity?, training: List<TrainingRegistrationEntity>) : List<TrainingEntity> {
        var trainingEntityList = mutableListOf<TrainingEntity>()

        for (i in training){
            val trainingEntity = TrainingEntity(
                training_id = null,
                institution = i.institution,
                degree = i.degree,
                start_year = i.start_year,
                end_year = i.end_year,
                country = i.country,
                city = i.city,
                state = i.state,
                notes = i.notes,
                profileEntity = savedProfileEntity
            )
            trainingRepository.save(trainingEntity)
            logger.info(LoggerMessages.LOG_SAVE_TRAINING(trainingEntity))
            trainingEntityList.add(trainingEntity)
        }
        return trainingEntityList
    }
}


