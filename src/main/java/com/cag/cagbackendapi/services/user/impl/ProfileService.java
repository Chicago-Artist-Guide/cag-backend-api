package com.cag.cagbackendapi.services.user.impl;

import com.cag.cagbackendapi.daos.impl.ProfileDao;
import com.cag.cagbackendapi.daos.impl.UserDao;
import com.cag.cagbackendapi.dtos.ProfileDto;
import com.cag.cagbackendapi.services.user.ProfileServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements ProfileServiceI {
    private final ProfileDao profileDao;

    @Autowired
    public ProfileService(ProfileDao profileDao){
        this.profileDao = profileDao;
    }

    @Override
    public ProfileDto registerProfile(ProfileDto profileDto) {
        return profileDao.saveProfile(profileDto);
    }
}
