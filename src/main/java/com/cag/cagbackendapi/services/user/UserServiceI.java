package com.cag.cagbackendapi.services.user;

import com.cag.cagbackendapi.dtos.UserRegistrationDto;
import com.cag.cagbackendapi.dtos.UserDto;

public interface UserServiceI {
    UserDto registerUser(UserRegistrationDto userRegistrationDto);
    UserDto deleteUser(String userId);
}
