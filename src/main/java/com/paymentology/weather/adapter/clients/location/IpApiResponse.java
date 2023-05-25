package com.paymentology.weather.adapter.clients.location;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.paymentology.weather.adapter.clients.location.Fail;
import com.paymentology.weather.adapter.clients.location.IpApiResponseResolver;
import com.paymentology.weather.adapter.clients.location.Success;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "status",
        defaultImpl = Fail.class
)
@JsonTypeIdResolver(IpApiResponseResolver.class)
public sealed interface IpApiResponse permits Success, Fail {

    String status();

    String query();
}
