package com.cag.cagbackendapi.services.user.impl;

import com.cag.cagbackendapi.constants.DetailedErrorMessages;
import com.cag.cagbackendapi.daos.impl.ProfileDao;
import com.cag.cagbackendapi.daos.impl.UserDao;
import com.cag.cagbackendapi.dtos.ProfileDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto;
import com.cag.cagbackendapi.errors.exceptions.BadRequestException;
import com.cag.cagbackendapi.errors.exceptions.ConflictException;
import com.cag.cagbackendapi.errors.exceptions.NotFoundException;
import com.cag.cagbackendapi.services.user.ProfileServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            throw new ConflictException(DetailedErrorMessages.USER_HAS_PROFILE, null);
        }

        validateProfileRegistrationDto(profileRegistrationDto);

        return profileDao.saveProfile(userUUID, profileRegistrationDto);
    }

    @Override
    public ProfileDto getProfile(String userId) {
        UUID userUUID = getUserUuidFromString(userId);

        var profileResponseDto = profileDao.getProfile(userUUID);

        if(profileResponseDto == null) {
            throw new NotFoundException(DetailedErrorMessages.PROFILE_NOT_FOUND, null);
        }

        return profileResponseDto;
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

    //I don't know why I have to make this public
    public String uploadFileS3(String userId, MultipartFile profilePhoto) {
        UUID profilePhotoId = UUID.randomUUID();
        //kept function in dao, feel free to correct if this is not a best practice
        String s3url = profileDao.uploadProfilePhotoS3(userId, profilePhotoId, profilePhoto);
        return s3url;
    }

    private void validateProfileRegistrationDto(ProfileRegistrationDto profileRegistrationDto){
        var badRequestMsg = "";

        if (profileRegistrationDto.getPronouns() == null || profileRegistrationDto.getPronouns().isBlank()) {
            badRequestMsg += DetailedErrorMessages.PRONOUN_REQUIRED;
        }

        if (profileRegistrationDto.getLgbtqplus_member() == null) {
            badRequestMsg += DetailedErrorMessages.LGBTQPLUS_MEMBER_REQUIRED;
        }

        if (profileRegistrationDto.getGender_identity() == null || profileRegistrationDto.getGender_identity().isBlank()) {
            badRequestMsg += DetailedErrorMessages.GENDER_IDENTITY_REQUIRED;
        }

        if (profileRegistrationDto.getHeight_inches() == null ) {
            badRequestMsg += DetailedErrorMessages.HEIGHT_INCHES_REQUIRED;
        }

        if (profileRegistrationDto.getBio() == null || profileRegistrationDto.getBio().isBlank()) {
            badRequestMsg += DetailedErrorMessages.BIO_REQUIRED;
        }

        /*if (profileRegistrationDto.getDemographic_union_status() == null || profileRegistrationDto.getDemographic_union_status().isBlank()) {
            badRequestMsg += DetailedErrorMessages.UNION_STATUS_MEMBER_REQUIRED;
        }*/

        if(!badRequestMsg.isEmpty()){
            throw new BadRequestException(badRequestMsg,null);
        }
    }
}
