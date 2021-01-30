package com.cag.cagbackendapi.services.user;

import com.cag.cagbackendapi.dtos.RegisterUserRequestDto;
import com.cag.cagbackendapi.dtos.UserResponseDto;

public interface UserServiceI {
    UserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto);
}
