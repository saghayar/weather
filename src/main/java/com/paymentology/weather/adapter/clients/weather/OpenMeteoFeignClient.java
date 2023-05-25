package com.paymentology.weather.adapter.clients.weather;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openMeteoFeignClient", url = "${service.open-meteo.url}",
        fallback = OpenMeteoFeignClientFallback.class,
        configuration = OpenMeteoApiFeignConfiguration.class
)
public interface OpenMeteoFeignClient {

    @GetMapping("/v1/forecast")
    @Cacheable(value = "weatherForecast", key = "#root.methodName", unless = "#result==null")
    ForecastResponse getForecast(@RequestParam("latitude") double latitude,
                                 @RequestParam("longitude") double longitude,
                                 @RequestParam("current_weather") boolean currentWeather,
                                 @RequestParam("temperature_unit") String temperatureUnit
    );
}

