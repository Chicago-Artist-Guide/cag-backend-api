package com.cag.cagbackendapi.services.organization;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    private final Logger logger;

    OrganizationService(Logger logger) {
        this.logger = logger;
    }

    public void testOrganizationService(String message) {
        logger.info(String.format("Test organization service: %s", message));
    }
}
