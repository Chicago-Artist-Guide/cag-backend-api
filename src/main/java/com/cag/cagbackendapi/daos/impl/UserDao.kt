package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.LoggerMessages.DELETE_USER
import com.cag.cagbackendapi.constants.LoggerMessages.GET_USER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_USER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_UPDATE_USER
import com.cag.cagbackendapi.daos.UserDaoI
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
import com.cag.cagbackendapi.entities.UserEntity
import com.cag.cagbackendapi.repositories.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDao : UserDaoI {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var logger: Logger

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun saveUser(userRegistrationDto: UserRegistrationDto): UserDto {
        logger.info(LOG_SAVE_USER(userRegistrationDto))

        val savedUserEntity = userRepository.save(userDtoToEntity(userRegistrationDto))
        return savedUserEntity.toDto()
    }

    override fun getUser(userUUID: UUID): UserDto? {
        logger.info(GET_USER(userUUID))

        val userEntity = userRepository.getByUserId(userUUID) ?: return null
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

    private fun userDtoToEntity(userRegistrationDto: UserRegistrationDto): UserEntity {
        return objectMapper.convertValue(userRegistrationDto, UserEntity::class.java)
    }
}
