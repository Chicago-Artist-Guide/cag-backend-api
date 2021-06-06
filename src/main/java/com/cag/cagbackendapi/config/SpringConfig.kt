package com.cag.cagbackendapi.config;

import com.cag.cagbackendapi.CagBackendApiApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
open class SpringConfig {

    @Bean
    open fun logger(): Logger {
        return LoggerFactory.getLogger(CagBackendApiApplication::class.java)
    }

    @Bean
    open fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper()
    }

    @Bean //NOTE: Used to render swagger-ui
    open fun defaultViewResolver(): InternalResourceViewResolver {
        return InternalResourceViewResolver()
    }

    @Bean
    open fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
