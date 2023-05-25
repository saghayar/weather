package com.paymentology.weather.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "client_api_keys")
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientApiKeyEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "revoked")
    private boolean revoked;

    @Column(name = "created_at")
    private Instant createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClientApiKeyEntity clientApiKeyEntity = (ClientApiKeyEntity) o;

        return Objects.equals(id, clientApiKeyEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
