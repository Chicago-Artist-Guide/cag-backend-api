package com.cag.cagbackendapi.daos

import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_USER
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.entities.User
import com.cag.cagbackendapi.repositories.OrganizationRepository
import com.cag.cagbackendapi.repositories.UserRepository
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserOrgDao {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var organizationRepository: OrganizationRepository

    @Autowired
    private lateinit var logger: Logger

    @Autowired
    private lateinit var modelMapper: ModelMapper

    fun saveUser(userDto: UserDto): UserDto {
        logger.info(LOG_SAVE_USER(userDto))
        val savedUser = userRepository.save(userDtoToEntity(userDto))
        return userEntityToDto(savedUser)
    }

    private fun userEntityToDto(user: User?): UserDto {
        return modelMapper.map(user, UserDto::class.java)
    }

    private fun userDtoToEntity(userDto: UserDto): User {
        var user = modelMapper.map(userDto, User::class.java)
        return user
    }
}