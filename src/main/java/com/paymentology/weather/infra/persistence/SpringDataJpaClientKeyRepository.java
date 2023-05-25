package com.paymentology.weather.infra.persistence;

import com.paymentology.weather.domain.model.entity.ClientApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaClientKeyRepository extends JpaRepository<ClientApiKeyEntity, Long> {

    Optional<ClientApiKeyEntity> findByApiKey(String apiKey);
}
