package com.paymentology.weather.application.exception;

public interface ErrorCode {
    String getPrefix();

    int getNumber();

    default String getStringRepresentation() {
        String padding = String.format(Formats.ERROR_CODE_PADDING_FORMAT.getValue(), getNumber())
                               .replace(' ', '0');
        return getPrefix() + "-" + padding;
    }
}
