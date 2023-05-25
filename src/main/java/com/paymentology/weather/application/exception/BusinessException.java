package com.paymentology.weather.application.exception;

/**
 * All business related exceptions
 */
public class BusinessException extends ApplicationBaseException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }

    public BusinessException(ErrorCode errorCode, String message, Exception e) {
        super(errorCode, message, e);
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message, null);
    }

    public BusinessException(ErrorCode errorCode, String message, Exception e, Object details) {
        super(errorCode, message, e, details);
    }

    public BusinessException(ErrorCode errorCode, String localizedErrorMessage, Exception e, String cause, Object details) {
        super(errorCode, localizedErrorMessage, e, cause, details);
    }
}
