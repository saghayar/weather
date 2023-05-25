package com.paymentology.weather.adapter.clients.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenMeteoFeignClientFallback implements OpenMeteoFeignClient {

    private final CacheManager cacheManager;

    @Override
    public ForecastResponse getForecast(double latitude,
                                        double longitude,
                                        boolean currentWeather,
                                        String temperatureUnit) {

        // Attempt to retrieve the cached result
        Cache cache = cacheManager.getCache("weatherForecast");
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get("getForecast");
            if (valueWrapper != null) {
                return (ForecastResponse) valueWrapper.get();
            }
        }
        return null;
    }
}
