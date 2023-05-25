package com.paymentology.weather.application.exception;

interface BaseServiceErrorCode extends ErrorCode {
    default String getPrefix() {
        return "WE";
    }
}
