package com.paymentology.weather.application.exception;

public enum Formats {
    ERROR_CODE_PADDING_FORMAT("%1$4s");

    private final String value;

    Formats(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
