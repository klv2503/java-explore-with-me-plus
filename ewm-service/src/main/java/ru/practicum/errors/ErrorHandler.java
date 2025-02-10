package ru.practicum.errors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handlerConstraintViolationException(final ConstraintViolationException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, "Got incorrect pathVariable");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handlerEntityNotFoundException(final EntityNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, "The required object was not found.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handlerDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, "Integrity constraint has been violated.");
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ApiError> handlerForbiddenActionException(final ForbiddenActionException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, "Action not allowed.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, "Argument type mismatch");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handlerOtherException(final Exception e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "Got 500 status Internal server error");
    }

    private ResponseEntity<ApiError> buildErrorResponse(Exception e, HttpStatus status, String message) {
        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.substring(className.lastIndexOf(".") + 1);
        log.error("\n{} error - Class: {}; Method: {}; Line: {}; \nMessage: {}",
                status, str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String statusStr = status.value() + " " + status.getReasonPhrase().replace(" ", "_");

        return ResponseEntity.status(status)
                .body(new ApiError(statusStr, message, e));
    }
}
