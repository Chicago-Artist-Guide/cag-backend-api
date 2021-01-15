package com.cag.cagbackendapi.config;

import com.cag.cagbackendapi.CagBackendApiApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(CagBackendApiApplication.class);
    }
}
