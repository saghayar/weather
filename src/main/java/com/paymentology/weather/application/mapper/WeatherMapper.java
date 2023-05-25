package com.paymentology.weather.application.mapper;

import com.paymentology.weather.adapter.clients.weather.CurrentWeather;
import com.paymentology.weather.domain.model.weather.TemperatureUnit;
import com.paymentology.weather.domain.model.weather.WeatherResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WeatherMapper {

    @Mapping(source = "currentWeather.windSpeed", target = "wind.speed")
    @Mapping(source = "currentWeather.windDirection", target = "wind.direction")
    @Mapping(source = "currentWeather.temperature", target = "temperature.value")
    @Mapping(source = "unit", target = "temperature.unit")
    @Mapping(source = "timestamp", target = "timestamp")
    WeatherResponseDto toWeather(CurrentWeather currentWeather, TemperatureUnit unit, Instant timestamp);

}
