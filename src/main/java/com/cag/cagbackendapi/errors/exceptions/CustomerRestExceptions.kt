package com.cag.cagbackendapi.errors.exceptions

import java.lang.RuntimeException

class BadRequestException(errorMessage: String?, cause: Throwable?) : RuntimeException(errorMessage, cause)

class UnauthorizedException(errorMessage: String?, cause: Throwable?) : RuntimeException(errorMessage, cause)

class ForbiddenException(errorMessage: String?, cause: Throwable?) : RuntimeException(errorMessage, cause)

class NotFoundException(errorMessage: String?, cause: Throwable?) : RuntimeException(errorMessage, cause)

class ConflictException(errorMessage: String?, cause: Throwable?) : RuntimeException(errorMessage, cause)

class ServiceUnavailableException(errorMessage: String?, cause: Throwable?) : RuntimeException(errorMessage, cause)

class InternalServerErrorException(errorMessage: String?, cause: Throwable?) : RuntimeException(errorMessage, cause)
