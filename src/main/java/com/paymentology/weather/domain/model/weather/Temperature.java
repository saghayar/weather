package com.paymentology.weather.domain.model.weather;

import lombok.Data;

@Data
public class Temperature {
    private double value;
    private TemperatureUnit unit;
}
