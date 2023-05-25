package com.paymentology.weather.adapter.clients.location;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IpApiFeignClientFallback implements IpApiFeignClient {

    private final CacheManager cacheManager;

    @Override
    public IpApiResponse getGeoLocationInfo(String query) {
        // Attempt to retrieve the cached result
        Cache cache = cacheManager.getCache("ipInfo");
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get("getGeoLocationInfo");
            if (valueWrapper != null) {
                return (IpApiResponse) valueWrapper.get();
            }
        }
        return null;
    }
}
