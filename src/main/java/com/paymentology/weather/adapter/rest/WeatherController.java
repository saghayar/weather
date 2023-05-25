package com.paymentology.weather.adapter.rest;

import com.paymentology.weather.domain.model.GeoLocation;
import com.paymentology.weather.domain.model.weather.TemperatureUnit;
import com.paymentology.weather.domain.model.weather.WeatherResponseDto;
import com.paymentology.weather.domain.service.GeoLocationService;
import com.paymentology.weather.domain.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final GeoLocationService geoLocationService;

    @GetMapping(path = "/weather")
    public ResponseEntity<WeatherResponseDto> getWeatherInfo(
            @RequestParam(defaultValue = "CELSIUS") TemperatureUnit unit,
            HttpServletRequest request
    ) {
        String ipAddress = request.getRemoteAddr();
        Optional<GeoLocation> geoLocation = Optional.ofNullable(ipAddress)
                                                    .flatMap(geoLocationService::detectByIp);

        if (geoLocation.isPresent()) {
            WeatherResponseDto weatherResponseDtoInfo = weatherService.getWeatherInfo(geoLocation.get(), unit);
            return ResponseEntity.ok(weatherResponseDtoInfo);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
