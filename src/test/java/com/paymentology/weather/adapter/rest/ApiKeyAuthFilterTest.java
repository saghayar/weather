package com.paymentology.weather.adapter.rest;

import com.paymentology.weather.domain.model.ClientApiKey;
import com.paymentology.weather.port.ClientKeyRepository;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ApiKeyAuthFilterTest {

    private static final String API_KEY = "key-1";

    @Mock
    ClientKeyRepository repo;

    @InjectMocks
    ApiKeyAuthFilter victim;

    MockHttpServletRequest rqt;
    MockHttpServletResponse rsp;
    MockFilterChain chain;

    @BeforeEach
    void setup() {
        rqt = new MockHttpServletRequest(Request.HttpMethod.GET.name(), "/api/v1/weather");
        rsp = new MockHttpServletResponse();
        chain = new MockFilterChain();
    }

    @Test
    void apiKeyIsPresentAndNotRevoked() throws Exception {
        //Arrange
        addRequestHeader(API_KEY);
        var key = new ClientApiKey(1, API_KEY, false);
        given(repo.find(API_KEY)).willReturn(Optional.of(key));

        //Act
        victim.doFilterInternal(rqt, rsp, chain);

        //Assert
        assertEquals(200, rsp.getStatus());
    }

    @Test
    void noHeaderAs403() throws Exception {
        //Act
        victim.doFilterInternal(rqt, rsp, chain);

        //Assert
        assertEquals(403, rsp.getStatus());
    }

    @Test
    void keyNotFoundAs403() throws Exception {
        //Arrange
        addRequestHeader(API_KEY);
        given(repo.find(API_KEY)).willReturn(Optional.empty());

        //Act
        victim.doFilterInternal(rqt, rsp, chain);

        //Assert
        assertEquals(403, rsp.getStatus());
    }

    @Test
    void keyRevokedAs403() throws Exception {
        //Arrange
        addRequestHeader(API_KEY);
        var key = new ClientApiKey(1, API_KEY, true);
        given(repo.find(API_KEY)).willReturn(Optional.of(key));

        //Act
        victim.doFilterInternal(rqt, rsp, chain);

        //Assert
        assertEquals(403, rsp.getStatus());
    }

    private void addRequestHeader(String headerValue) {
        rqt.addHeader("X-API-KEY", headerValue);
    }

}
