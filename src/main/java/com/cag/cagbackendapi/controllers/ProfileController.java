package com.cag.cagbackendapi.controllers;


import com.cag.cagbackendapi.dtos.ProfileDto;
import com.cag.cagbackendapi.dtos.ProfileExtraInfoDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationExtraInfoDto;
import com.cag.cagbackendapi.services.user.impl.ProfileService;
import com.cag.cagbackendapi.services.user.impl.ProfileServiceExtraInfo;
import com.cag.cagbackendapi.services.validation.impl.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/user/{userId}/profile")
@CrossOrigin
public class ProfileController {

    private final ValidationService validationService;
    private final ProfileService profileService;
    private final ProfileServiceExtraInfo profileServiceExtraInfo;


    @Autowired
    ProfileController(ProfileService profileService, ValidationService validationService, ProfileServiceExtraInfo profileServiceExtraInfo) {
        this.validationService = validationService;
        this.profileService = profileService;
        this.profileServiceExtraInfo = profileServiceExtraInfo;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProfileDto> registerProfile(
            @RequestHeader("authKey") String authKey,
            @PathVariable("userId") String userId,
            @RequestBody ProfileRegistrationDto profileRegistrationDto
    ) {
        this.validationService.validateAuthKey(authKey);
        ProfileDto profileResponseDto = this.profileService.registerProfile(userId, profileRegistrationDto);

        return new ResponseEntity<>(profileResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(value= "/profilePhoto",
            produces = {"text/plain"},
            consumes = {"multipart/form-data"})
    @ResponseBody
    public String uploadProfilePicture(
            @RequestHeader("authKey") String authKey,
            @PathVariable("userId") String userId,
            @RequestPart("file") MultipartFile profilePhoto
    ) {
        this.validationService.validateAuthKey(authKey);
        String profile_photo_url = this.profileService.uploadFileS3(userId, profilePhoto);
        return profile_photo_url;
    }

    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProfileDto> getProfile(
            @RequestHeader("authKey") String authKey,
            @PathVariable("userId") String userId
    ) {
        this.validationService.validateAuthKey(authKey);
        ProfileDto profileResponseDto = this.profileService.getProfile(userId);

        return new ResponseEntity<>(profileResponseDto, HttpStatus.OK);
    }

    @PostMapping(value = "/register/registertwo")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProfileExtraInfoDto> registerProfileExtraInfo(
            @RequestHeader("authKey") String authKey,
            @PathVariable("userId") String userId,
            @RequestBody ProfileRegistrationExtraInfoDto profileRegistrationExtraInfoDto
    ) {
        this.validationService.validateAuthKey(authKey);
        ProfileExtraInfoDto profileExtraInfoResponseDto = this.profileServiceExtraInfo.registerProfileExtraInfo(userId, profileRegistrationExtraInfoDto);

        return new ResponseEntity<>(profileExtraInfoResponseDto, HttpStatus.CREATED);
        //return null;
    }
}

