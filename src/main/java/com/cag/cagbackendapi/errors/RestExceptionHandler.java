package com.cag.cagbackendapi.errors;

import com.cag.cagbackendapi.constants.RestErrorMessages;
import com.cag.cagbackendapi.errors.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = { BadRequestException.class })
    protected ResponseEntity<ErrorDetails> handleBadRequestException(Exception ex, WebRequest request) {

        logger.error(RestErrorMessages.BAD_REQUEST_MESSAGE, ex);

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                RestErrorMessages.BAD_REQUEST_MESSAGE,
                ex.getLocalizedMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { UnauthorizedException.class })
    protected ResponseEntity<ErrorDetails> handleUnauthorizedException(Exception ex, WebRequest request) {

        logger.error(RestErrorMessages.UNAUTHORIZED_MESSAGE, ex);

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                RestErrorMessages.UNAUTHORIZED_MESSAGE,
                ex.getLocalizedMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<ErrorDetails> handleNotFoundException(Exception ex, WebRequest request) {

        logger.error(RestErrorMessages.NOT_FOUND_MESSAGE, ex);

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                RestErrorMessages.NOT_FOUND_MESSAGE,
                ex.getLocalizedMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { InternalServerErrorException.class, RuntimeException.class })
    protected ResponseEntity<ErrorDetails> handleInternalServerError(Exception ex, WebRequest request) {

        logger.error(RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE, ex);

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                RestErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE,
                ex.getLocalizedMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { ConflictException.class })
    protected ResponseEntity<ErrorDetails> handleConflictException(Exception ex, WebRequest request) {

        logger.error(RestErrorMessages.CONFLICT_MESSAGE, ex);

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                RestErrorMessages.CONFLICT_MESSAGE,
                ex.getLocalizedMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}
