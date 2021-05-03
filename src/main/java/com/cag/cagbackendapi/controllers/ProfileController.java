package com.cag.cagbackendapi.controllers;


import com.cag.cagbackendapi.dtos.ProfileDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto;
import com.cag.cagbackendapi.services.user.impl.ProfileService;
import com.cag.cagbackendapi.services.validation.impl.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user/{userId}/profile")
@CrossOrigin
public class ProfileController {

    private final ValidationService validationService;
    private final ProfileService profileService;


    @Autowired
    ProfileController(ProfileService profileService, ValidationService validationService) {
        this.validationService = validationService;
        this.profileService = profileService;
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
}






