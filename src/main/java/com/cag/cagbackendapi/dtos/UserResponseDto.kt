package com.cag.cagbackendapi.dtos;

import java.util.UUID

data class UserResponseDto(var user_id: UUID?,
                           var first_name: String?,
                           var last_name: String?,
                           var email: String?,
                           var active_status: Boolean?,
                           var session_id: String?)
