package ru.practicum.errors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handlerConstraintViolationException(final ConstraintViolationException e) {

        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.substring(className.lastIndexOf(".") + 1);
        log.info("\nConstraintViolationException error - Class: {}; Method: {}; Line: {}; \nMessage: {}",
                str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        String statusStr = HttpStatus.BAD_REQUEST.value() + " "
                + HttpStatus.BAD_REQUEST.getReasonPhrase().replace(" ", "_");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(statusStr, "Got incorrect pathVariable", e));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handlerEntityNotFoundException(final EntityNotFoundException e) {

        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.substring(className.lastIndexOf(".") + 1);
        log.info("\nEntityNotFoundException error - Class: {}; Method: {}; Line: {}; \nMessage: {}",
                str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        String statusStr = HttpStatus.NOT_FOUND.value() + " "
                + HttpStatus.NOT_FOUND.getReasonPhrase().replace(" ", "_");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(statusStr, "The required object was not found.", e));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handlerDataIntegrityViolationException(final DataIntegrityViolationException e) {

        StackTraceElement sElem = e.getStackTrace()[0];
        String className = sElem.getClassName();
        String str = className.substring(className.lastIndexOf(".") + 1);
        log.info("\nDataIntegrityViolationException error - Class: {}; Method: {}; Line: {}; \nMessage: {}",
                str, sElem.getMethodName(), sElem.getLineNumber(), e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        String statusStr = HttpStatus.CONFLICT.value() + " "
                + HttpStatus.CONFLICT.getReasonPhrase().replace(" ", "_");
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(statusStr, "Integrity constraint has been violated.", e));
    }

    @ExceptionHandler
    @ResponseStatus
    public ResponseEntity<ApiError> handlerOtherException(final Exception e) {
        log.error("\nGot 500 status Internal server error {}", e.getMessage(), e);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        String statusStr = HttpStatus.INTERNAL_SERVER_ERROR.value() + " "
                + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().replace(" ", "_");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(statusStr, "Got 500 status Internal server error", e));
    }

}
