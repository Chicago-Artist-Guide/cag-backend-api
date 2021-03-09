package com.cag.cagbackendapi.daos;

import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import java.util.*

interface UserDaoI {
    fun saveUser(userRegistrationDto: UserRegistrationDto): UserDto
    fun updateUser(userDto: UserDto): UserDto?
    fun getUser(userUUID: UUID): UserDto?
}
