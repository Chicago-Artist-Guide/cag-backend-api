package com.cag.cagbackendapi.constants

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
    const val EXAMPLE_MSG = "Example error message."
}
