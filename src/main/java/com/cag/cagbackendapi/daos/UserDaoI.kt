package com.cag.cagbackendapi.daos;

import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserResponseDto
import java.util.*

interface UserDaoI {
    fun saveUser(registerUserRequestDto: RegisterUserRequestDto): UserResponseDto
    fun deleteUser(userUUID: UUID): UserResponseDto?
    fun updateUser(userDto: UserDto): UserResponseDto?
    fun getUser(userUUID: UUID): UserResponseDto?
}
