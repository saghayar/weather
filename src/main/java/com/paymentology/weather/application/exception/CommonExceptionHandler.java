package com.paymentology.weather.application.exception;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class,
            HttpMediaTypeNotAcceptableException.class, UnsatisfiedServletRequestParameterException.class,
            MissingServletRequestParameterException.class,
            MethodNotAllowedException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidRequests(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .description(ex.getMessage())
                                                   .code(ServiceErrorCodes.HTTP_REQUEST_ERROR.getStringRepresentation())
                                                   .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logException(ex);
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                                       .map(fieldError -> fieldError.getField() + "-" + fieldError.getDefaultMessage())
                                       .distinct()
                                       .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .code(ServiceErrorCodes.VALIDATION_FAILED.getStringRepresentation())
                                                   .description(ServiceErrorCodes.VALIDATION_FAILED.name())
                                                   .details(errorMessages)
                                                   .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errorResponse);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .code(ServiceErrorCodes.VALIDATION_FAILED.getStringRepresentation())
                                                   .description(ServiceErrorCodes.VALIDATION_FAILED.name())
                                                   .details(message)
                                                   .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errorResponse);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        logException(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .description(ex.getMessage())
                                                   .code(ServiceErrorCodes.HTTP_REQUEST_ERROR.getStringRepresentation())
                                                   .build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                             .body(errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logException(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .code(ServiceErrorCodes.RESOURCE_NOT_FOUND.getStringRepresentation())
                                                   .description(ex.getMessage())
                                                   .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(errorResponse);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOthers(Exception ex) {
        logException(ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .code(ServiceErrorCodes.INTERNAL_ERROR.getStringRepresentation())
                                                   .description(ex.getMessage())
                                                   .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ResponseBody
    @ExceptionHandler({ApplicationBaseException.class})
    //TODO :: Consider having error code to status code mappers to be able to return relevant status codes
    public ResponseEntity<ErrorResponse> handleServiceException(ApplicationBaseException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        logException(ex);

        String stringRepresentation = errorCode.getStringRepresentation();
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .code(stringRepresentation)
                                                   .description(nonNull(ex.getMessage()) ? ex.getMessage() : errorCode.toString())
                                                   .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    public void logException(Exception e) {
        logger.error("Unexpected exception: {}", e.getMessage());
    }

}
