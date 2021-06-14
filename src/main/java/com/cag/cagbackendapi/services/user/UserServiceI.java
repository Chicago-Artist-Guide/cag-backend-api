package com.cag.cagbackendapi.services.user;

import com.cag.cagbackendapi.dtos.UserDto;
import com.cag.cagbackendapi.dtos.UserLoginDto;
import com.cag.cagbackendapi.dtos.UserRegistrationDto;
import com.cag.cagbackendapi.dtos.UserUpdateDto;

public interface UserServiceI {
    UserDto registerUser(UserRegistrationDto userRegistrationDto);
    UserDto getByUserId(String userId);
    UserDto loginUser(UserLoginDto userLoginDto);
    UserDto updateUser(String userId, UserUpdateDto userUpdateDto);
    UserDto deleteUser(String userId);
}
