package com.cag.cagbackendapi.services.user;

import com.cag.cagbackendapi.dtos.ProfileDto;

public interface ProfileServiceI {
    ProfileDto registerProfile(ProfileDto profileDto);
}
