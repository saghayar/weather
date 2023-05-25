package com.paymentology.weather.application.exception;

public enum ServiceErrorCodes implements BaseServiceErrorCode {
    UNAUTHORIZED_CLIENT(1),
    INVALID_TOKEN(2),
    INTERNAL_ERROR(3),
    FORBIDDEN(4),
    HTTP_REQUEST_ERROR(5),
    RESOURCE_NOT_FOUND(6),
    VALIDATION_FAILED(7);

    private final int number;

    ServiceErrorCodes(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }


}
