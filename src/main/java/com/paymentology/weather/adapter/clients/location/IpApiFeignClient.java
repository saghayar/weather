package com.paymentology.weather.adapter.clients.location;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "locationApiClient", url = "${service.ip-api.url}",
        fallback = IpApiFeignClientFallback.class)
public interface IpApiFeignClient {

    @GetMapping("/json/{query}")
    @Cacheable(value = "ipInfo", key = "#root.methodName", unless = "#result==null")
    IpApiResponse getGeoLocationInfo(@PathVariable("query") String query);
}

