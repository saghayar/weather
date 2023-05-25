package com.paymentology.weather.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ConcurrentMapCache weatherForecast = new ConcurrentMapCache("weatherForecast");
        ConcurrentMapCache ipInfo = new ConcurrentMapCache("ipInfo");
        cacheManager.setCaches(Arrays.asList(weatherForecast, ipInfo));
        return cacheManager;
    }
}
