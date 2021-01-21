package com.cag.cagbackendapi.daos;

import com.cag.cagbackendapi.dtos.UserDto

interface UserDaoI {
    fun saveUser(userDto: UserDto): UserDto
}
