package com.paymentology.weather.domain.service;

import com.paymentology.weather.domain.model.GeoLocation;
import com.paymentology.weather.domain.model.weather.TemperatureUnit;
import com.paymentology.weather.domain.model.weather.WeatherResponseDto;

public interface WeatherService {

    WeatherResponseDto getWeatherInfo(GeoLocation location, TemperatureUnit unit);

}
