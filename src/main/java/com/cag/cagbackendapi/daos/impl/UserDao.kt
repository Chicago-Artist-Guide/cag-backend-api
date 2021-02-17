package com.cag.cagbackendapi.daos.impl

import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_USER
import com.cag.cagbackendapi.constants.LoggerMessages.LOG_UPDATE_USER
import com.cag.cagbackendapi.daos.UserDaoI
import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserResponseDto
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

    override fun saveUser(registerUserRequestDto: RegisterUserRequestDto): UserResponseDto {
        logger.info(LOG_SAVE_USER(registerUserRequestDto))

        val savedUserEntity = userRepository.save(userDtoToEntity(registerUserRequestDto))
        return savedUserEntity.toDto()
    }

    private fun userDtoToEntity(registerUserRequestDto: RegisterUserRequestDto): UserEntity {
        return modelMapper.map(registerUserRequestDto, UserEntity::class.java)
    }

    override fun updateUser(userDto: UserDto): UserResponseDto? {
        logger.info(LOG_UPDATE_USER(userDto))

        val userEntity = userRepository.getByUserId(userDto.user_id) ?: return null

        userEntity.first_name = userDto.first_name
        userEntity.last_name = userDto.last_name
        userEntity.email = userDto.email

        val userResponseEntity = userRepository.save(userEntity)

        return userResponseEntity.toDto()
    }
}
