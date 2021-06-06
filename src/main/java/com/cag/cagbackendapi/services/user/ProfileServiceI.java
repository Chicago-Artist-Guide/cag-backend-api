package com.cag.cagbackendapi.services.user;

import com.cag.cagbackendapi.dtos.ProfileDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto;

public interface ProfileServiceI {
    ProfileDto registerProfile(String user_Id, ProfileRegistrationDto profileRegistrationDto);
    ProfileDto getProfile(String userId);
}
