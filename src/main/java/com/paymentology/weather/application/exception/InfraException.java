package com.paymentology.weather.application.exception;

/**
 * Exception class to handle infrastructure failures
 */
//TODO :: Error codes can be separated to handle exceptions from different layers
public class InfraException extends ApplicationBaseException {
    public InfraException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InfraException(ErrorCode errorCode, Exception e) {
        super(errorCode, e);
    }

    public InfraException(ErrorCode errorCode, String message, Exception e) {
        super(errorCode, message, e);
    }

    public InfraException(ErrorCode errorCode, String message, Exception e, Object details) {
        super(errorCode, message, e, details);
    }

    public InfraException(ErrorCode errorCode, String localizedErrorMessage, Exception e, String cause, Object details) {
        super(errorCode, localizedErrorMessage, e, cause, details);
    }
}
