package com.cag.cagbackendapi.daos;

import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
import java.util.*

interface UserDaoI {
    fun saveUser(userRegistrationDto: UserRegistrationDto): UserDto
    fun updateUser(userId: UUID, userUpdateDto: UserUpdateDto): UserDto?
    fun getUser(userUUID: UUID): UserDto?
    fun deleteUser(userUUID: UUID): UserDto?

}
