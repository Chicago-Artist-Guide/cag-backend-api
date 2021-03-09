package com.cag.cagbackendapi.daos;

import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.dtos.UserDto
import java.util.*

interface UserDaoI {
    fun saveUser(registerUserRequestDto: RegisterUserRequestDto): UserDto
    fun updateUser(userDto: UserDto): UserDto?
    fun getUser(userUUID: UUID): UserDto?
}
