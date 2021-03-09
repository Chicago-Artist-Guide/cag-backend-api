package com.cag.cagbackendapi.services.user;

import com.cag.cagbackendapi.dtos.RegisterUserRequestDto;
import com.cag.cagbackendapi.dtos.UserDto;

public interface UserServiceI {
    UserDto registerUser(RegisterUserRequestDto registerUserRequestDto);
}
