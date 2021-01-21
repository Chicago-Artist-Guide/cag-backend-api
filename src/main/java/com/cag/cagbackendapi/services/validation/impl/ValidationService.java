package com.cag.cagbackendapi.services.validation.impl;

import com.cag.cagbackendapi.constants.DetailedErrorMessages;
import com.cag.cagbackendapi.errors.exceptions.UnauthorizedException;
import com.cag.cagbackendapi.services.validation.ValidationServiceI;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidationService implements ValidationServiceI {

    @Value("${spring.authKey}")
    private String authKey;

    private final Logger logger;

    public ValidationService(Logger logger) {
        this.logger = logger;
    }

    public void validateAuthKey(String requestAuthKey) {
        if (requestAuthKey == null || requestAuthKey.isEmpty()) {
            logger.error(DetailedErrorMessages.MISSING_AUTH_KEY);
            throw new UnauthorizedException(DetailedErrorMessages.MISSING_AUTH_KEY, null);
        }

        if (!requestAuthKey.equals(authKey)) {
            logger.error(DetailedErrorMessages.WRONG_AUTH_KEY);
            throw new UnauthorizedException(DetailedErrorMessages.WRONG_AUTH_KEY, null);
        }
    }
}
