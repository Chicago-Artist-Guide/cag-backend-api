package com.cag.cagbackendapi.services.user;

import com.cag.cagbackendapi.daos.UserOrgDao;
import com.cag.cagbackendapi.dtos.UserDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserOrgDao userOrgDao;

    public UserService(UserOrgDao userOrgDao) {
        this.userOrgDao = userOrgDao;
    }

    public UserDto registerUser(UserDto userDto) {
        return userOrgDao.saveUser(userDto);
    }
}
