package com.cag.cagbackendapi.util

import com.cag.cagbackendapi.daos.impl.ProfileDao
import com.cag.cagbackendapi.daos.impl.UserDao
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TestDataCreatorService {
    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var profileDao: ProfileDao

    fun createValidUser(password: String): UserDto {
        val userRegistrationDto = UserRegistrationDto(
            first_name = "Larry",
            last_name = "Tester",
            email = "LarryTester@aol.com",
            pass = password,
            agreed_18 = true,
            agreed_privacy = true
        )

        return userDao.saveUser(userRegistrationDto)
    }

    fun deleteUser(userUUID: UUID): UserDto? {
        return userDao.deleteUser(userUUID)
    }
}