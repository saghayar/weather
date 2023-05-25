package com.paymentology.weather.adapter.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.paymentology.weather.BaseControllerTestCase;
import com.paymentology.weather.WireMockInitializer;
import com.paymentology.weather.adapter.clients.weather.CurrentWeather;
import com.paymentology.weather.adapter.clients.weather.ForecastResponse;
import com.paymentology.weather.domain.model.weather.WeatherResponseDto;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Sql(value = {"/testdata/clean-data.sql", "/testdata/all-data.sql"}, executionPhase =
        Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ContextConfiguration(initializers = {WireMockInitializer.class})
class WeatherControllerIntegrationTest extends BaseControllerTestCase {

    private static final String TEST_API_KEY = "test-token";
    private static final String TEST_IP = "91.216.134.195";
    private static final String FORECAST_URL = "/v1/forecast?latitude=50.0&longitude=43.0&current_weather=true&temperature_unit=celsius";

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private CacheManager cacheManager;

    private UrlPattern geoLocationInfoUrlPattern;
    private UrlPattern weatherForecastUrlPattern;

    @BeforeEach
    void setUp() {
        //Set up WireMock stubs for the external API
        this.geoLocationInfoUrlPattern = urlEqualTo("/json/" + TEST_IP);
        this.weatherForecastUrlPattern = urlEqualTo(FORECAST_URL);
    }

    @SneakyThrows
    @Test
    void shouldReturnWeatherDataSuccessfully() {
        //Arrange
        stubForHappyScenario();

        //Act
        ResponseEntity<WeatherResponseDto> responseEntity = makeGetRequest("/weather",
                                                                           populateHeaders(),
                                                                           WeatherResponseDto.class);

        //Assert
        WeatherResponseDto responseDto = responseEntity.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),

                () -> JSONAssert.assertEquals(readResourceFile("__files/api-weather-success-response.json"),
                                              getAsString(responseDto), JSONCompareMode.STRICT)
        );


        // Verify that the expected request was made to WireMock
        verifyCacheEntryCreation(responseDto);
    }

    @SneakyThrows
    @Test
    void testWeatherAuthFailure() {
        //Arrange
        stubForHappyScenario();

        //Act
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Forwarded-For", TEST_IP);

        ResponseEntity<WeatherResponseDto> responseEntity = makeGetRequest("/weather",
                                                                           headers,
                                                                           WeatherResponseDto.class);

        //Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void shouldReturnDataFromCacheWhenFailureHappens() {
        // Arrange
        stubForHappyScenario();

        //Act
        ResponseEntity<WeatherResponseDto> responseEntity = makeGetRequest("/weather", populateHeaders(),
                                                                           WeatherResponseDto.class);

        //Assert happy scenario
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verifyCacheEntryCreation(responseEntity.getBody());

        wireMockServer.resetAll();
        stubFor5xxFailure();

        //Act
        ResponseEntity<WeatherResponseDto> responseFromCache = makeGetRequest("/weather", populateHeaders(),
                                                                              WeatherResponseDto.class);

        //Assert failure scenario - data has been taken from cache through fallback method
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verifyCacheEntryCreation(responseFromCache.getBody());
    }

    private void verifyCacheEntryCreation(WeatherResponseDto weatherResponseDto) {
        //verify that the responses have been cached
        Cache cache = cacheManager.getCache("weatherForecast");
        assertNotNull(cache);

        Cache.ValueWrapper valueWrapper = cache.get("getForecast");
        assertNotNull(valueWrapper);

        ForecastResponse forecastResponse = (ForecastResponse) valueWrapper.get();

        CurrentWeather currentWeather = forecastResponse.getCurrentWeather();
        assertAll(
                () -> assertEquals(currentWeather.getWindDirection(), weatherResponseDto.getWind().direction()),
                () -> assertEquals(currentWeather.getWindSpeed(), weatherResponseDto.getWind().speed()),
                () -> assertEquals(currentWeather.getTemperature(), weatherResponseDto.getTemperature().getValue())
        );
    }

    private void stubFor5xxFailure() {
        wireMockServer.stubFor(get(geoLocationInfoUrlPattern)
                                       .willReturn(aResponse()
                                                           .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));

        wireMockServer.stubFor(get(weatherForecastUrlPattern)
                                       .willReturn(aResponse()
                                                           .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }

    private void stubForHappyScenario() {
        wireMockServer.stubFor(get(geoLocationInfoUrlPattern)
                                       .willReturn(aResponse()
                                                           .withStatus(HttpStatus.OK.value())
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBodyFile("location-success-response.json")));

        wireMockServer.stubFor(get(weatherForecastUrlPattern)
                                       .willReturn(aResponse()
                                                           .withStatus(HttpStatus.OK.value())
                                                           .withHeader("Content-Type", "application/json")
                                                           .withBodyFile("weather-success-response.json")));
    }

    private HttpHeaders populateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-KEY", TEST_API_KEY);
        headers.add("X-Forwarded-For", TEST_IP);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
