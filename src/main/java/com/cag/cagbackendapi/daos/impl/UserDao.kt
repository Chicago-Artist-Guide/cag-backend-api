package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.LoggerMessages.GET_USER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_USER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_UPDATE_USER
import com.cag.cagbackendapi.daos.UserDaoI
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.entities.UserEntity
import com.cag.cagbackendapi.repositories.UserRepository
import org.modelmapper.ModelMapper
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
    private lateinit var modelMapper: ModelMapper

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

    private fun userDtoToEntity(userRegistrationDto: UserRegistrationDto): UserEntity {
        return modelMapper.map(userRegistrationDto, UserEntity::class.java)
    }

    override fun updateUser(userId: UUID, userDto: UserDto): UserDto? {
        logger.info(LOG_UPDATE_USER(userDto))

        val userEntity = userRepository.getByUserId(userId) ?: return null

        userEntity.setFirstName(userDto.first_name)
        userEntity.setLastName(userDto.last_name)
        userEntity.setEmailJava(userDto.email)

        val userResponseEntity = userRepository.save(userEntity)

        return userResponseEntity.toDto()
    }
}
