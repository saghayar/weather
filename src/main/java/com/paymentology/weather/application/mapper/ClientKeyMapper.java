package com.paymentology.weather.application.mapper;

import com.paymentology.weather.domain.model.ClientApiKey;
import com.paymentology.weather.domain.model.entity.ClientApiKeyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientKeyMapper {

    ClientApiKey toDto(ClientApiKeyEntity entity);
}
