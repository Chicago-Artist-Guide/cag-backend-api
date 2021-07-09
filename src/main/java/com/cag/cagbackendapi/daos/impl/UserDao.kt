package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.DetailedErrorMessages
import com.cag.cagbackendapi.constants.LoggerMessages.DELETE_USER
import com.cag.cagbackendapi.constants.LoggerMessages.GET_USER
import com.cag.cagbackendapi.constants.LoggerMessages.LOGIN_USER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_USER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_UPDATE_USER
import com.cag.cagbackendapi.daos.UserDaoI
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
import com.cag.cagbackendapi.entities.UserEntity
import com.cag.cagbackendapi.errors.exceptions.BadRequestException
import com.cag.cagbackendapi.repositories.UserRepository
import com.cag.cagbackendapi.services.uuid.impl.UuidService
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDao : UserDaoI {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var logger: Logger

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var uuidService: UuidService

    override fun saveUser(userRegistrationDto: UserRegistrationDto): UserDto {
        val encodedPassword = passwordEncoder.encode(userRegistrationDto.pass)
        userRegistrationDto.pass = encodedPassword

        logger.info(LOG_SAVE_USER(userRegistrationDto))

        val userEntity = UserEntity(
            userId = null,
            first_name = userRegistrationDto.first_name,
            last_name = userRegistrationDto.last_name,
            email = userRegistrationDto.email,
            pass = userRegistrationDto.pass,
            active_status = true,
            session_id = uuidService.generateRandomUUID().toString(),
            img_url = null,
            agreed_18 = userRegistrationDto.agreed_18,
            agreed_privacy = userRegistrationDto.agreed_privacy
        )

        val savedUserEntity = userRepository.save(userEntity)
        return savedUserEntity.toDto()
    }

    override fun getUser(userUUID: UUID): UserDto? {
        logger.info(GET_USER(userUUID))

        val userEntity = userRepository.getByUserId(userUUID) ?: return null
        return userEntity.toDto()
    }

    override fun loginAndGetUser(userUUID: UUID, pass: String): UserDto? {
        logger.info(LOGIN_USER(userUUID))

        val userEntity = userRepository.getByUserId(userUUID) ?: return null

        val signInSuccessful = passwordEncoder.matches(pass, userEntity.pass)

        if (!signInSuccessful) {
            throw BadRequestException(DetailedErrorMessages.INCORRECT_PASSWORD, null)
        }

        return userEntity.toDto()
    }

    fun getUserByEmail(email: String): UserDto? {
        //logger.info(GET_USER(email))

        val userEntity = userRepository.getByEmail(email) ?: return null
        return userEntity.toDto()
    }

    override fun updateUser(userId: UUID, userUpdateDto: UserUpdateDto): UserDto? {
        logger.info(LOG_UPDATE_USER(userUpdateDto))

        val userEntity = userRepository.getByUserId(userId) ?: return null

        userEntity.first_name = userUpdateDto.first_name
        userEntity.last_name = userUpdateDto.last_name
        userEntity.email = userUpdateDto.email

        val userResponseEntity = userRepository.save(userEntity)

        return userResponseEntity.toDto()
    }

    override fun deleteUser(userUUID: UUID): UserDto? {
        logger.info(DELETE_USER(userUUID))

        val deleteUserEntity = userRepository.getByUserId(userUUID) ?: return null
        userRepository.deleteById(userUUID)
        return deleteUserEntity.toDto()
    }
}
