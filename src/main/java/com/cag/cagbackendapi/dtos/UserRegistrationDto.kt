package com.cag.cagbackendapi.dtos;

data class UserRegistrationDto(
    var first_name: String?,
    var last_name: String?,
    var email: String?,
    var agreed_18: Boolean?)
