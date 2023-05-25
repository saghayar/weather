package com.paymentology.weather.application.service;

import com.paymentology.weather.adapter.clients.weather.CurrentWeather;
import com.paymentology.weather.adapter.clients.weather.ForecastResponse;
import com.paymentology.weather.adapter.clients.weather.OpenMeteoFeignClient;
import com.paymentology.weather.application.mapper.WeatherMapper;
import com.paymentology.weather.domain.model.GeoLocation;
import com.paymentology.weather.domain.model.weather.TemperatureUnit;
import com.paymentology.weather.domain.model.weather.WeatherResponseDto;
import com.paymentology.weather.domain.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OpenMeteoWeatherService implements WeatherService {

    private final OpenMeteoFeignClient openMeteoFeignClient;
    private final WeatherMapper weatherMapper;

    @Override
    public WeatherResponseDto getWeatherInfo(GeoLocation geoLocation, TemperatureUnit unit) {
        final ForecastResponse forecast = openMeteoFeignClient.getForecast(geoLocation.latitude(),
                                                                           geoLocation.longitude(),
                                                                           true,
                                                                           unitOf(unit));

        return Optional.ofNullable(forecast.getCurrentWeather())
                       .map(mapToWeather(unit, forecast))
                       .orElseThrow(() -> new IllegalArgumentException("Unexpected response received"));
    }

    private Function<CurrentWeather, WeatherResponseDto> mapToWeather(TemperatureUnit unit, ForecastResponse forecast) {
        return cw -> weatherMapper.toWeather(cw, unit,
                                             convertTimeToInstant(cw.getTime(),
                                                                  forecast.getTimezone()));
    }

    public static Instant convertTimeToInstant(String time, String timezone) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(timezone));
        return zonedDateTime.toInstant();
    }

    private String unitOf(TemperatureUnit unit) {
        if (unit != TemperatureUnit.KELVIN) {
            return unit.name().toLowerCase(Locale.ROOT);
        }
        throw new IllegalArgumentException("Unsupported temperature unit " + unit);
    }
}
