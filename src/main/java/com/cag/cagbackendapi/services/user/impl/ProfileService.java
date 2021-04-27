package com.cag.cagbackendapi.services.user.impl;

import com.cag.cagbackendapi.constants.DetailedErrorMessages;
import com.cag.cagbackendapi.daos.impl.ProfileDao;
import com.cag.cagbackendapi.daos.impl.UserDao;
import com.cag.cagbackendapi.dtos.ProfileDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto;
import com.cag.cagbackendapi.errors.exceptions.BadRequestException;
import com.cag.cagbackendapi.errors.exceptions.ConflictException;
import com.cag.cagbackendapi.services.user.ProfileServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService implements ProfileServiceI {
    private final ProfileDao profileDao;

    @Autowired
    public ProfileService(ProfileDao profileDao){
        this.profileDao = profileDao;
    }

    @Override
    public ProfileDto registerProfile(String userId, ProfileRegistrationDto profileRegistrationDto) {
        UUID userUUID = getUserUuidFromString(userId);

        if(profileDao.getUserWithProfile(userUUID) != null){
            throw new ConflictException(DetailedErrorMessages.USER_HAS_PROFILE,null);
        }

        return profileDao.saveProfile(userUUID, profileRegistrationDto);
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

    /*private void validateProfileRegistrationDto(ProfileRegistrationDto profileRegistrationDto){
        var badRequestMsg = "";


        if(!badRequestMsg.isEmpty()){
            throw new BadRequestException(badRequestMsg,null);
        }
    }*/
}
