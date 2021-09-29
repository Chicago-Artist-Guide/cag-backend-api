package com.cag.cagbackendapi.services.user.impl;

import com.cag.cagbackendapi.constants.DetailedErrorMessages;
import com.cag.cagbackendapi.daos.impl.ProfileDao;
import com.cag.cagbackendapi.daos.impl.ProfileExtraInfoDao;
import com.cag.cagbackendapi.dtos.ProfileDto;
import com.cag.cagbackendapi.dtos.ProfileExtraInfoDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationExtraInfoDto;
import com.cag.cagbackendapi.errors.exceptions.BadRequestException;
import com.cag.cagbackendapi.errors.exceptions.ConflictException;
import com.cag.cagbackendapi.errors.exceptions.NotFoundException;
import com.cag.cagbackendapi.services.user.ProfileServiceExtraInfoI;
import com.cag.cagbackendapi.services.user.ProfileServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileServiceExtraInfo implements ProfileServiceExtraInfoI {
    private final ProfileExtraInfoDao profileExtraInfoDao;

    @Autowired
    public ProfileServiceExtraInfo(ProfileExtraInfoDao profileExtraInfoDao){
        this.profileExtraInfoDao = profileExtraInfoDao;
    }

    @Override
    public ProfileExtraInfoDto registerProfileExtraInfo(String userId, ProfileRegistrationExtraInfoDto profileRegistrationExtraInfoDto) {
        UUID userUUID = getUserUuidFromString(userId);

        /*if(profileDao.getUserWithProfile(userUUID) != null){
            throw new ConflictException(DetailedErrorMessages.USER_HAS_PROFILE, null);
        }*/

        //validateProfileRegistrationDto(profileRegistrationDto);

        return profileExtraInfoDao.saveProfileExtraInfo(userUUID, profileRegistrationExtraInfoDto);

    }

    /*@Override
    public ProfileDto registerProfile(String userId, ProfileRegistrationDto profileRegistrationDto) {
        UUID userUUID = getUserUuidFromString(userId);

        if(profileDao.getUserWithProfile(userUUID) != null){
            throw new ConflictException(DetailedErrorMessages.USER_HAS_PROFILE, null);
        }

        validateProfileRegistrationDto(profileRegistrationDto);

        return profileDao.saveProfile(userUUID, profileRegistrationDto);
    }*/


   /* public ProfileDto getProfile(String userId) {
        UUID userUUID = getUserUuidFromString(userId);

        var profileResponseDto = profileExtraInfoDao.getProfile(userUUID);

        if(profileResponseDto == null) {
            throw new NotFoundException(DetailedErrorMessages.PROFILE_NOT_FOUND, null);
        }

        return profileResponseDto;
    }*/

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




}
