package com.cag.cagbackendapi.services.user.impl;
import com.cag.cagbackendapi.constants.DetailedErrorMessages;
import com.cag.cagbackendapi.daos.impl.UserDao;
import com.cag.cagbackendapi.dtos.UserRegistrationDto;
import com.cag.cagbackendapi.dtos.UserDto;
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

    public UserDto registerUser(UserRegistrationDto userRegistrationDto) {
        var badRequestMsg = "";

        if (userRegistrationDto.getFirst_name() == null || userRegistrationDto.getFirst_name().isBlank()) {
            badRequestMsg += DetailedErrorMessages.FIRST_NAME_REQUIRED;
        }

        if (userRegistrationDto.getLast_name() == null || userRegistrationDto.getLast_name().isBlank()) {
            badRequestMsg += DetailedErrorMessages.LAST_NAME_REQUIRED;
        }

        if (userRegistrationDto.getEmail() == null || userRegistrationDto.getEmail().isBlank()) {
            badRequestMsg += DetailedErrorMessages.EMAIL_REQUIRED;
        }

        if (userRegistrationDto.getAgreed_18() == null || !userRegistrationDto.getAgreed_18()) {
            badRequestMsg += DetailedErrorMessages.MUST_BE_18;
        }

        if (!badRequestMsg.isEmpty()) {
            throw new BadRequestException(badRequestMsg, null);
        }

        return userDao.saveUser(userRegistrationDto);
    }

    public UserDto updateUser(UserDto userRequestDto) {
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

        if (userResponseDto == null){
            throw new NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null);
        }

        return userResponseDto;
    }

    public UserDto getByUserId(String userId) {

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

    @Override
    public UserDto deleteUser(String userId) {

        UUID userUUID;

        if (userId == "" || userId == null) {
            throw new BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null);
        }

        try {
            userUUID = UUID.fromString(userId);
        } catch(Exception ex){
            throw new BadRequestException(DetailedErrorMessages.INVALID_USER_ID, ex);
        }

        UserDto userResponseDto = userDao.deleteUser(userUUID);

        if (userResponseDto == null) {
            throw new NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null);
        }
        return userResponseDto;
    }
}
