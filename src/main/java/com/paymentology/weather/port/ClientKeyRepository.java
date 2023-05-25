package com.paymentology.weather.port;

import com.paymentology.weather.domain.model.ClientApiKey;

import java.util.Optional;

public interface ClientKeyRepository {

    Optional<ClientApiKey> find(String apiKey);

}
