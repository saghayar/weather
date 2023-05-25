package com.paymentology.weather.infra.persistence;

import com.paymentology.weather.application.mapper.ClientKeyMapper;
import com.paymentology.weather.domain.model.ClientApiKey;
import com.paymentology.weather.port.ClientKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class JpaClientKeyRepository implements ClientKeyRepository {

    private final SpringDataJpaClientKeyRepository repository;
    private final ClientKeyMapper mapper;

    @Override
    public Optional<ClientApiKey> find(String apiKey) {
        return repository.findByApiKey(apiKey).map(mapper::toDto);
    }

}
