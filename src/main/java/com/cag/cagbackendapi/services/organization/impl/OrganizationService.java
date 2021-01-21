package com.cag.cagbackendapi.services.organization.impl;

import com.cag.cagbackendapi.services.organization.OrganizationServiceI;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService implements OrganizationServiceI {

    private final Logger logger;

    OrganizationService(Logger logger) {
        this.logger = logger;
    }

    public void testOrganizationService(String message) {
        logger.info(String.format("Test organization service: %s", message));
    }
}
