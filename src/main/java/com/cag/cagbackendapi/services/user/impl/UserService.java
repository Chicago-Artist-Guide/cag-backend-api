package com.cag.cagbackendapi.services.user.impl;

import com.cag.cagbackendapi.constants.DetailedErrorMessages;
import com.cag.cagbackendapi.daos.impl.UserDao;
import com.cag.cagbackendapi.dtos.RegisterUserRequestDto;
import com.cag.cagbackendapi.dtos.UserResponseDto;
import com.cag.cagbackendapi.errors.exceptions.BadRequestException;
import com.cag.cagbackendapi.services.user.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceI {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto) {
        var badRequestMsg = "";

        if (registerUserRequestDto.getFirst_name() == null || registerUserRequestDto.getFirst_name().isBlank()) {
            badRequestMsg += DetailedErrorMessages.NAME_REQUIRED;
        }

        if (registerUserRequestDto.getEmail() == null || registerUserRequestDto.getEmail().isBlank()) {
            badRequestMsg += DetailedErrorMessages.EMAIL_REQUIRED;
        }

        if (!badRequestMsg.isEmpty()) {
            throw new BadRequestException(badRequestMsg, null);
        }

        return userDao.saveUser(registerUserRequestDto);
    }
}
