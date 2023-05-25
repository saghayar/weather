package com.paymentology.weather.adapter.clients.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrentWeather {

    @JsonProperty("temperature")
    private Double temperature;
    @JsonProperty("windspeed")
    @NotNull
    private Double windSpeed;
    @JsonProperty("winddirection")
    @NotNull
    private Double windDirection;
    @JsonProperty("weathercode")
    private Integer weathercode;
    @JsonProperty("is_day")
    private Integer isDay;
    @JsonProperty("time")
    private String time;

}

