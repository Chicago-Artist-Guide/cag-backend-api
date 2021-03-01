package com.cag.cagbackendapi.services.user.impl;

import com.cag.cagbackendapi.constants.DetailedErrorMessages;
import com.cag.cagbackendapi.constants.RestErrorMessages;
import com.cag.cagbackendapi.daos.impl.UserDao;
import com.cag.cagbackendapi.dtos.RegisterUserRequestDto;
import com.cag.cagbackendapi.dtos.UserDto;
import com.cag.cagbackendapi.dtos.UserResponseDto;
import com.cag.cagbackendapi.errors.exceptions.BadRequestException;
import com.cag.cagbackendapi.errors.exceptions.NotFoundException;
import com.cag.cagbackendapi.services.user.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
            badRequestMsg += DetailedErrorMessages.FIRST_NAME_REQUIRED;
        }

        if (registerUserRequestDto.getLast_name() == null || registerUserRequestDto.getLast_name().isBlank()) {
            badRequestMsg += DetailedErrorMessages.LAST_NAME_REQUIRED;
        }

        if (registerUserRequestDto.getEmail() == null || registerUserRequestDto.getEmail().isBlank()) {
            badRequestMsg += DetailedErrorMessages.EMAIL_REQUIRED;
        }

        if (!badRequestMsg.isEmpty()) {
            throw new BadRequestException(badRequestMsg, null);
        }

        return userDao.saveUser(registerUserRequestDto);
    }

    public UserResponseDto updateUser(UserDto userRequestDto) {
        var badRequestMsg = "";

        if (userRequestDto.getFirst_name() == null || userRequestDto.getFirst_name().isBlank()) {
            badRequestMsg += DetailedErrorMessages.FIRST_NAME_REQUIRED;
        }

        if (userRequestDto.getLast_name() == null || userRequestDto.getLast_name().isBlank()) {
            badRequestMsg += DetailedErrorMessages.LAST_NAME_REQUIRED;
        }

        if (userRequestDto.getEmail() == null || userRequestDto.getEmail().isBlank()) {
            badRequestMsg += DetailedErrorMessages.EMAIL_REQUIRED;
        }

        if (!badRequestMsg.isEmpty()) {
            throw new BadRequestException(badRequestMsg, null);
        }

        if(userRequestDto.getUser_id() == null){
            throw new BadRequestException(DetailedErrorMessages.INVALID_UUID, null);
        }

        var userResponseDto = userDao.updateUser(userRequestDto);

        if (userRequestDto == null){
            throw new NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null);
        }

        return userResponseDto;
    }

    public UserResponseDto getByUserId(String userId) {

        if(userId == null || userId == "") {
            throw new BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null);
        }

        UUID userUUID;

        try {
            userUUID = UUID.fromString(userId);
        } catch(Exception ex){
            throw new BadRequestException(DetailedErrorMessages.INVALID_USER_ID, ex);
        }

        var userResponseDto = userDao.getUser(userUUID);

        if(userResponseDto == null) {
            throw new NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null);
        }

        return userResponseDto;
    }
}
