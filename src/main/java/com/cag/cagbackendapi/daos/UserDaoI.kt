package com.cag.cagbackendapi.daos;

import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.dtos.UserResponseDto
import java.util.*

interface UserDaoI {
    fun saveUser(registerUserRequestDto: RegisterUserRequestDto): UserResponseDto
    fun deleteUser(userUUID: UUID): UserResponseDto?
}
