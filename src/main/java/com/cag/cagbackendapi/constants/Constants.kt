package com.cag.cagbackendapi.constants

import com.cag.cagbackendapi.dtos.ProfileRegistrationDto
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
import com.cag.cagbackendapi.entities.*
import java.util.*

object RestErrorMessages {
    const val BAD_REQUEST_MESSAGE = "400: Bad Request"
    const val UNAUTHORIZED_MESSAGE = "401: Unauthorized Request"
    const val FORBIDDEN_MESSAGE = "403: Forbidden"
    const val NOT_FOUND_MESSAGE = "404: Not Found"
    const val CONFLICT_MESSAGE = "409: Conflict"
    const val UNSUPPORTED_MEDIA_TYPE_MESSAGE = "415: Unsupported Media Type"
    const val INTERNAL_SERVER_ERROR_MESSAGE = "500: Internal Server Error"
    const val SERVICE_UNAVAILABLE_MESSAGE = "503: Service Unavailable"
}

object DetailedErrorMessages {
    const val MISSING_AUTH_KEY = "Request is unauthorized."
    const val WRONG_AUTH_KEY = "Invalid auth key."
    const val FIRST_NAME_REQUIRED = "First name is required. "
    const val LAST_NAME_REQUIRED = "Last name is required. "
    const val EMAIL_REQUIRED = "Email is required. "
    const val PASSWORD_REQUIRED = "Password is required. "
    const val INVALID_USER_ID = "Invalid user ID. "
    const val USER_NOT_FOUND = "User not found. "
    const val INCORRECT_PASSWORD = "Incorrect password. "
    const val INVALID_UUID = "Invalid Id. "
    const val MUST_BE_18 = "You must be eighteen years or older to use Chicago Artist Guide. "
    const val MUST_AGREE_PRIVACY = "You must accept our Privacy Agreement to use Chicago Artist Guide. "
    const val USER_HAS_PROFILE = "This user already has a profile. "
    const val PRONOUN_REQUIRED = "Pronoun identity is required. "
    const val LGBTQPLUS_MEMBER_REQUIRED = "LGBTQ+ identity is required. "
    const val GENDER_IDENTITY_REQUIRED = "Gender identity is required. "
    const val HEIGHT_INCHES_REQUIRED = "Height is required. "
    const val BIO_REQUIRED = "Bio is required. "
    const val PROFILE_NOT_FOUND = "Profile not found. "
    const val UNION_STATUS_MEMBER_REQUIRED = "Union status is required. "
    const val UNION_STATUS_NOT_SUPPORTED = "Union Status not supported."
    const val EMAIL_ALREADY_EXISTS = "This email is already being used on another account."
    const val ETHNICITY_NOT_SUPPORTED = "Ethnicity not supported."
    const val ETHNICITY_REQUIRED = "Ethnicity required."
    const val AGE_INCREMENT_MEMBER_REQUIRED = "Age increment is required. "
    const val AGE_INCREMENT_NOT_SUPPORTED = "Age increment not supported."
    const val PROFILE_PHOTO_UPLOAD_FAIL = "Profile Photo upload fail."
}

object LoggerMessages {
    fun LOG_SAVE_USER(userRegistration: UserRegistrationDto): String {
        return "Save user: $userRegistration"
    }
    fun LOG_UPDATE_USER(userDto: UserUpdateDto): String {
        return "Update user: $userDto"
    }
    fun GET_USER(userId: UUID): String {
        return "Get user: $userId"
    }
    fun LOGIN_USER(userId: UUID): String {
        return "Login user: $userId"
    }
    fun DELETE_USER(userUUID: UUID): String {
        return "Delete user: $userUUID"
    }
    fun LOG_SAVE_PROFILE(profileRegistrationDto: ProfileRegistrationDto): String {
        return "Save profile: $profileRegistrationDto"
    }
    fun GET_PROFILE(userId: UUID): String {
        return "Get profile: $userId"
    }
    fun LOG_SAVE_UNION_STATUS_MEMBER(unionStatusMemberEntity: UnionStatusMemberEntity): String {
        return "Save Union Status Member: $unionStatusMemberEntity"
    }
    fun LOG_SAVE_SKILL_MEMBER(skillMemberEntity: SkillMemberEntity): String {
        return "Save Skill Member: $skillMemberEntity"
    }
    fun LOG_SAVE_ETHNICITY_MEMBER (ethnicityMemberEntity: EthnicityMemberEntity): String {
        return "Save Ethnicity Member: $ethnicityMemberEntity"
    }
    fun LOG_SAVE_AGE_INCREMENT_MEMBER(ageIncrementMemberEntity: AgeIncrementMemberEntity): String {
        return "Save Age Increment Member Entity: $ageIncrementMemberEntity"
    }
    fun LOG_SAVE_AWARD(awardEntity: AwardEntity): String {
        return "Save Award Entity: $awardEntity"
    }
    fun LOG_SAVE_TRAINING(trainingEntity: TrainingEntity): String {
        return "Save Award Entity: $trainingEntity"
    }
}