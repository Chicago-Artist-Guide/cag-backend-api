package com.cag.cagbackendapi.constants

import com.cag.cagbackendapi.dtos.RegisterUserRequestDto
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserResponseDto
import java.util.*

object RestErrorMessages {
    const val BAD_REQUEST_MESSAGE = "400: Bad Request"
    const val UNAUTHORIZED_MESSAGE = "401: Unauthorized Request"
    const val FORBIDDEN_MESSAGE = "403: Forbidden"
    const val NOT_FOUND_MESSAGE = "404: Not Found"
    const val CONFLICT_MESSAGE = "409: Conflict"
    const val UNSUPPORTED_MEDIA_TYPE_MESSAGE = "415: Unsupported Media Type"
    const val INTERNAL_SERVER_ERROR_MESSAGE = "500: Internal Server Error"
    const val SERVICE_UNAVAILABLE_MESSAGE = "503: Service Unavailable"
}

object DetailedErrorMessages {
    const val MISSING_AUTH_KEY = "Request is unauthorized."
    const val WRONG_AUTH_KEY = "Invalid auth key."
    const val FIRST_NAME_REQUIRED = "First name is required. "
    const val LAST_NAME_REQUIRED = "Last name is required. "
    const val EMAIL_REQUIRED = "Email is required. "
    const val INVALID_USER_ID = "Invalid user ID. "
    const val USER_NOT_FOUND = "User not found. "
    const val INVALID_UUID = "Invalid UserId. "
}

object LoggerMessages {
    fun LOG_SAVE_USER(registerUserRequest: RegisterUserRequestDto): String {
        return "Save user: $registerUserRequest"
    }

    fun LOG_UPDATE_USER(userDto: UserDto): String {
        return "Update user: $userDto"
    }
    fun GET_USER(userId: UUID): String {
        return "Get user: $userId"
    }
}
