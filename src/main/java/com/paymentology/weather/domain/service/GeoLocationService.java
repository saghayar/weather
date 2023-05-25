package com.paymentology.weather.domain.service;

import com.paymentology.weather.domain.model.GeoLocation;

import java.util.Optional;

public interface GeoLocationService {

    Optional<GeoLocation> detectByIp(String ip);

}
