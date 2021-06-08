package com.cag.cagbackendapi.dtos;

data class UserRegistrationDto(
    var first_name: String? = null,
    var last_name: String? = null,
    var email: String? = null,
    var pass: String? = null,
    var agreed_18: Boolean? = null,
    var agreed_privacy: Boolean? = null)