package com.paymentology.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTestCase extends BaseIntegrationTestCase {

    @Value("${api.version}")
    private String apiVersion;

    @Value("${local.server.port}")
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    protected <R> ResponseEntity<R> makeGetRequest(String path, HttpHeaders headers, Class<R> responseClass) {
        final String baseUrl = "http://localhost:" + serverPort + "/api/" + apiVersion;
        return restTemplate.exchange(baseUrl + path, HttpMethod.GET, new HttpEntity<>(headers), responseClass);
    }

}
