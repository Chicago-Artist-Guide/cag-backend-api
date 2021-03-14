package com.cag.cagbackendapi.services.user.impl;
import com.cag.cagbackendapi.constants.DetailedErrorMessages;
import com.cag.cagbackendapi.daos.impl.UserDao;
import com.cag.cagbackendapi.dtos.UserRegistrationDto;
import com.cag.cagbackendapi.dtos.UserDto;
import com.cag.cagbackendapi.dtos.UserUpdateDto;
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

    @Override
    public UserDto registerUser(UserRegistrationDto userRegistrationDto) {
        validateUserRegistrationDto(userRegistrationDto);

        return userDao.saveUser(userRegistrationDto);
    }

    @Override
    public UserDto getByUserId(String userId) {
        UUID userUUID = getUserUuidFromString(userId);

        var userResponseDto = userDao.getUser(userUUID);

        if(userResponseDto == null) {
            throw new NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null);
        }

        return userResponseDto;
    }

    @Override
    public UserDto updateUser(String userId, UserUpdateDto userUpdateDto) {
        UUID userUUID = getUserUuidFromString(userId);

        validateUserUpdateDto(userUpdateDto);

        var userResponseDto = userDao.updateUser(userUUID, userUpdateDto);

        if (userResponseDto == null) {
            throw new NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null);
        }

        return userResponseDto;
    }

    @Override
    public UserDto deleteUser(String userId) {
        UUID userUUID = getUserUuidFromString(userId);

        UserDto userResponseDto = userDao.deleteUser(userUUID);

        if (userResponseDto == null) {
            throw new NotFoundException(DetailedErrorMessages.USER_NOT_FOUND, null);
        }

        return userResponseDto;
    }

    private void validateUserUpdateDto(UserUpdateDto userDto) {
        var badRequestMsg = "";

        if (userDto.getFirst_name() == null || userDto.getFirst_name().isBlank()) {
            badRequestMsg += DetailedErrorMessages.FIRST_NAME_REQUIRED;
        }

        if (userDto.getLast_name() == null || userDto.getLast_name().isBlank()) {
            badRequestMsg += DetailedErrorMessages.LAST_NAME_REQUIRED;
        }

        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            badRequestMsg += DetailedErrorMessages.EMAIL_REQUIRED;
        }

        if (!badRequestMsg.isEmpty()) {
            throw new BadRequestException(badRequestMsg, null);
        }
    }

    private UUID getUserUuidFromString(String userId) {
        if(userId == null || userId.equals("")) {
            throw new BadRequestException(DetailedErrorMessages.INVALID_USER_ID, null);
        }

        UUID userUUID;

        try {
            userUUID = UUID.fromString(userId);
        } catch(Exception ex){
            throw new BadRequestException(DetailedErrorMessages.INVALID_USER_ID, ex);
        }

        return userUUID;
    }

    private void validateUserRegistrationDto(UserRegistrationDto userRegistrationDto) {
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
    }
}
