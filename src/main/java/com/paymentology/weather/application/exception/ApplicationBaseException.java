package com.paymentology.weather.application.exception;

public class ApplicationBaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;
    private String localizedErrorMessage;
    private String cause;
    private Object details;

    public ApplicationBaseException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public ApplicationBaseException(ErrorCode errorCode, Exception e) {
        this(errorCode, null, e);
    }

    public ApplicationBaseException(ErrorCode errorCode, String message, Exception e) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public ApplicationBaseException(ErrorCode errorCode, String message, Exception e, Object details) {
        super(message, e);
        this.errorCode = errorCode;
        this.details = details;
    }

    public ApplicationBaseException(ErrorCode errorCode, String localizedErrorMessage, Exception e, String cause, Object details) {
        super(localizedErrorMessage, e);
        this.errorCode = errorCode;
        this.localizedErrorMessage = localizedErrorMessage;
        this.cause = cause;
        this.details = details;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object getDetails() {
        return details;
    }

}
