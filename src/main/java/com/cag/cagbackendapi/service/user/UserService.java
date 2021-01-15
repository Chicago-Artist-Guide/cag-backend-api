package com.cag.cagbackendapi.service.user;

import com.cag.cagbackendapi.model.UserModel;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Logger logger;

    public UserService(Logger logger) {
        this.logger = logger;
    }

    public void registerUser(UserModel userModel) {
        logger.info(String.format("Testing user service: %s", userModel));
    }
}
