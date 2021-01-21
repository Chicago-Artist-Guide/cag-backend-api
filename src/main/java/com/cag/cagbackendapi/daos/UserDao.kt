package com.cag.cagbackendapi.daos

import com.cag.cagbackendapi.constants.LoggerMessages.LOG_SAVE_USER
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.entities.UserEntity
import com.cag.cagbackendapi.repositories.UserRepository
import org.modelmapper.ModelMapper
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDao {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var logger: Logger

    @Autowired
    private lateinit var modelMapper: ModelMapper

    fun saveUser(userDto: UserDto): UserDto {
        if (userDto.id == null) {
            userDto.id = UUID.randomUUID()
        }

        logger.info(LOG_SAVE_USER(userDto))

        val savedUserEntity = userRepository.save(userDtoToEntity(userDto))
        return savedUserEntity.toDto()
    }

    private fun userDtoToEntity(userDto: UserDto): UserEntity {
        return modelMapper.map(userDto, UserEntity::class.java)
    }
}
