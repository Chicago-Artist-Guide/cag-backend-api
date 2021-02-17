package com.cag.cagbackendapi.daos;

import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserResponseDto

interface UserDaoI {
    fun saveUser(registerUserRequestDto: RegisterUserRequestDto): UserResponseDto
    fun updateUser(userDto: UserDto): UserResponseDto?
}
