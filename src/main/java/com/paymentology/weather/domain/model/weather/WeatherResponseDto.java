package com.paymentology.weather.domain.model.weather;

import lombok.Data;

import java.time.Instant;

@Data
public class WeatherResponseDto {
    private Temperature temperature;
    private Wind wind;
    private Instant timestamp;
}
