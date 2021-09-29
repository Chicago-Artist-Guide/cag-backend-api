package com.cag.cagbackendapi.services.user;

import com.cag.cagbackendapi.dtos.ProfileDto;
import com.cag.cagbackendapi.dtos.ProfileExtraInfoDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationDto;
import com.cag.cagbackendapi.dtos.ProfileRegistrationExtraInfoDto;

public interface ProfileServiceExtraInfoI {
    ProfileExtraInfoDto registerProfileExtraInfo(String userId, ProfileRegistrationExtraInfoDto profileRegistrationExtraInfoDto);
    //ProfileDto getProfile(String userId);
}
