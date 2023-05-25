package com.paymentology.weather.adapter.clients.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IpApiResponseCodecTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldBeDecodedIntoFail() throws JsonProcessingException {
        String json = getResourceFileAsString("__files/fail-response.json");
        IpApiResponse result = objectMapper.readValue(json, IpApiResponse.class);
        Fail expected = new Fail("reserved range", "0.0.0.0");
        assertEquals(expected, result);
    }

    @Test
    void shouldBeDecodedIntoSuccess() throws JsonProcessingException {
        String json = getResourceFileAsString("__files/location-success-response_ut.json");
        IpApiResponse result = objectMapper.readValue(json, IpApiResponse.class);
        Success expected = buildSuccess();
        assertEquals(expected, result);
    }

    private Success buildSuccess() {
        return new Success(
                "81.198.87.60",
                "Latvia",
                "LV",
                "RIX",
                "Riga",
                "Riga",
                "LV-1063",
                56.9496,
                24.0978,
                "Europe/Riga",
                "Lattelekom",
                "Bridge Group",
                "AS12578 SIA Tet"
        );
    }

    private String getResourceFileAsString(String fileName) {
        try {
            return new String(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName)).readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource file: " + fileName, e);
        }
    }
}
