package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.LoggerMessages.GET_PROFILE
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_AGE_INCREMENT_MEMBER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_ETHNICITY_MEMBER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_PROFILE
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_SKILL_MEMBER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_UNION_STATUS_MEMBER
import com.cag.cagbackendapi.daos.ProfileDaoI
import com.cag.cagbackendapi.dtos.ProfileDto
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.entities.*
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.errors.exceptions.NotFoundException
import com.cag.cagbackendapi.errors.exceptions.UploadFailedException
import com.cag.cagbackendapi.repositories.*
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import java.util.*
import java.io.*

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
    private lateinit var ageIncrementMemberRepository: AgeIncrementMemberRepository

    @Autowired
    private lateinit var ageIncrementRepository: AgeIncrementRepository

    @Autowired
    private lateinit var profilePhotoRepository: ProfilePhotoRepository

    @Autowired
    private lateinit var logger: Logger

    private var badRequestMsg: String = ""

    override fun saveProfile(userId: UUID, profileRegistrationDto: ProfileRegistrationDto): ProfileDto {
        clearBadRequestMsg()
        logger.info(LOG_SAVE_PROFILE(profileRegistrationDto))

        //val unionStatusEntity = validateUnionStatus(profileRegistrationDto.demographic_union_status)
        //val profilePhotoEntity = profileRegistrationDto.profile_photo_url
        //validateAgeIncrement(profileRegistrationDto.age_increment)

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
        //val unionStatusEntity = validateUnionStatus(profileRegistrationDto.demographic_union_status)
        //saveUnionStatusMemberEntity(savedProfileEntity, unionStatusEntity!!)

        //check for existing union and create if not found
        if(profileRegistrationDto.demographic_union_status != null) {
            saveUnionStatusMember(savedProfileEntity, profileRegistrationDto.demographic_union_status!!)
        }

        //check for existing skill and create if not found
        if(profileRegistrationDto.actor_skills != null) {
            saveUserSkills(savedProfileEntity, profileRegistrationDto.actor_skills!!)
        }

        //check for existing ethnicities and create if not found
        if(profileRegistrationDto.actor_ethnicity != null) {
            saveUserEthnicity(savedProfileEntity, profileRegistrationDto.actor_ethnicity!!)
        }

        if(profileRegistrationDto.age_increment != null && profileRegistrationDto.age_increment!![0] != "string"){
            saveAgeIncrementMemberEntity(savedProfileEntity, profileRegistrationDto.age_increment!!)
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

    override fun saveUserEthnicity(savedProfileEntity: ProfileEntity?, actorEthnicities: List<String>?) {
        if (actorEthnicities != null) {
            for (i in actorEthnicities){
                val actorEthnicityEntity = getUserEthnicity(i.lowercase())

                val ethnicityMemberEntity = EthnicityMemberEntity(
                        null,
                        savedProfileEntity,
                        actorEthnicityEntity
                )
                ethnicityMemberRepository.save(ethnicityMemberEntity)
                logger.info(LOG_SAVE_ETHNICITY_MEMBER(ethnicityMemberEntity))
            }
        }
    }

    override fun saveUnionStatusMember(savedProfileEntity: ProfileEntity?, unionStatusName: String?) {
        if (unionStatusName != null) {
                val unionStatusEntity = getUnionStatus(unionStatusName.lowercase())

            val unionStatusMemberEntity = UnionStatusMemberEntity(
                    null,
                    savedProfileEntity,
                    unionStatusEntity
            )

            logger.info(LOG_SAVE_UNION_STATUS_MEMBER(unionStatusMemberEntity))
            unionStatusMemberRepository.save(unionStatusMemberEntity)
        }
    }

    override fun uploadProfilePhotoS3(userId: String, profilePhotoId: UUID, profilePhoto: MultipartFile): String {
        return uploadS3(userId, profilePhotoId, profilePhoto);
    }

    /*private fun saveUnionStatusMemberEntity(savedProfileEntity: ProfileEntity, unionStatusEntity: UnionStatusEntity){
        val unionStatusMemberEntity = UnionStatusMemberEntity(
                null,
                savedProfileEntity,
                unionStatusEntity
        )

        logger.info(LOG_SAVE_UNION_STATUS_MEMBER(unionStatusMemberEntity))
        unionStatusMemberRepository.save(unionStatusMemberEntity)
    }*/

    /*private fun validateUnionStatus(unionStatusName: String?): UnionStatusEntity? {
        val unionStatusEntity = unionStatusRepository.getByName(unionStatusName)

        if (unionStatusEntity == null) {
            badRequestMsg += DetailedErrorMessages.UNION_STATUS_NOT_SUPPORTED
        }

        return unionStatusEntity
    }*/

    private fun getUnionStatus(unionStatusName: String?): UnionStatusEntity {
        return if (unionStatusRepository.getByName(unionStatusName) != null ) {
            unionStatusRepository.getByName(unionStatusName)
        } else {
            val unionStatusEntity = UnionStatusEntity(null, unionStatusName)
            unionStatusRepository.save(unionStatusEntity)
        }
    }

    private fun getUserSkill(userSkill: String?): SkillEntity {
        return if (skillRepository.getByName(userSkill) != null ) {
            skillRepository.getByName(userSkill)
        } else {
            val userSkillEntity = SkillEntity(null, userSkill)
            skillRepository.save(userSkillEntity)
        }
    }

    private fun getUserEthnicity(ethnicity: String?): EthnicityEntity {
        return if (ethnicityRepository.getByName(ethnicity) != null ) {
            ethnicityRepository.getByName(ethnicity)
        } else {
            val userEthnicityEntity = EthnicityEntity(null, ethnicity)
            ethnicityRepository.save(userEthnicityEntity)
        }
    }

    private fun saveAgeIncrementMemberEntity(savedProfileEntity: ProfileEntity, ageIncrements: List<String>) {

        for (i in ageIncrements) {
            val ageIncrementEntity = ageIncrementRepository.getByAges(i)

            val ageIncrementMemberEntity = AgeIncrementMemberEntity(
                    null,
                    savedProfileEntity,
                    ageIncrementEntity
            )

            ageIncrementMemberRepository.save(ageIncrementMemberEntity)
            logger.info(LOG_SAVE_AGE_INCREMENT_MEMBER(ageIncrementMemberEntity))
        }
    }

    private fun clearBadRequestMsg() {
        badRequestMsg = ""
    }

    private fun createS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
    }

    private fun convertFileJava(profilePhoto: MultipartFile): File {
        val profilePhotoFile: File = File(profilePhoto.originalFilename);
        val fos: FileOutputStream = FileOutputStream(profilePhotoFile);
        fos.write(profilePhoto.bytes);
        fos.close();
        return profilePhotoFile;
    }

    private fun uploadS3(userId: String, profilePhotoId: UUID, profilePhoto: MultipartFile): String {
        val s3 = createS3Client()
        val convertedProfilePhoto: File = convertFileJava(profilePhoto)
        val key = "user/$userId/${profilePhotoId.toString()}"
        try {
            val response = s3.putObject("profilepicture-dev", key, convertedProfilePhoto)
        } catch (e:AmazonServiceException) {
            UploadFailedException(DetailedErrorMessages.PROFILE_PHOTO_UPLOAD_FAIL, null)
            return("")
        }
        return ("https://profilepicture-dev.s3.us-east-2.amazonaws.com/user/$key")
    }
}
