package com.paymentology.weather.application.service;

import com.paymentology.weather.adapter.clients.location.Fail;
import com.paymentology.weather.adapter.clients.location.IpApiFeignClient;
import com.paymentology.weather.adapter.clients.location.IpApiResponse;
import com.paymentology.weather.adapter.clients.location.Success;
import com.paymentology.weather.domain.model.GeoLocation;
import com.paymentology.weather.domain.service.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IpApiGeoLocationService implements GeoLocationService {

    private static final Logger log = LoggerFactory.getLogger(IpApiGeoLocationService.class);

    private final IpApiFeignClient ipApiFeignClient;

    @Override
    public Optional<GeoLocation> detectByIp(String ip) {
        Optional<InetAddress> resolve = resolve(ip);
        if (resolve.isPresent()) {
            IpApiResponse response = ipApiFeignClient.getGeoLocationInfo(ip);
            return switch (response) {
                case Success s -> Optional.of(new GeoLocation(s.lat(), s.lon()));
                case Fail f -> {
                    log.error("Failed to fetch geo location by ip {}, result {}", ip, f);
                    yield Optional.empty();
                }
            };
        } else {
            return Optional.empty();
        }
    }

    private Optional<InetAddress> resolve(String ip) {
        try {
            return Optional.of(InetAddress.getByName(ip));
        } catch (UnknownHostException ex) {
            return Optional.empty();
        }
    }

}
