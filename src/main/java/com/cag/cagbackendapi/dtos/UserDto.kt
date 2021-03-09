package com.cag.cagbackendapi.dtos;

import java.util.UUID

data class UserDto(
    var user_id: UUID?,
    var first_name: String?,
    var last_name: String?,
    var email: String?,
    var active_status: Boolean?,
    var session_id: String?,
    var img_url: String?,
    var agreed_18: Boolean?)
