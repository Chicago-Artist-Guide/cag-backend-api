package com.cag.cagbackendapi.util

import org.springframework.test.context.ActiveProfilesResolver
import org.springframework.test.context.support.DefaultActiveProfilesResolver

class SpringCommandLineProfileResolver : ActiveProfilesResolver {
    private val defaultActiveProfilesResolver = DefaultActiveProfilesResolver()

    override fun resolve(testClass: Class<*>): Array<String> {
        val springProfileKey = "spring.profiles.active"
        return if (System.getProperties().containsKey(springProfileKey) && System.getProperty(springProfileKey).isNotEmpty())
            System.getProperty(springProfileKey).split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        else
            defaultActiveProfilesResolver.resolve(testClass)
    }
}
