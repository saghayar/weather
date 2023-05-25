package com.paymentology.weather.adapter.clients.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import feign.Response;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Configuration
public class OpenMeteoApiFeignConfiguration {
    private static final Logger log = LoggerFactory.getLogger(OpenMeteoApiFeignConfiguration.class);

    @Bean
    Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }

    @Bean
    ErrorDecoder feignErrorDecoder() {
        return new ErrorDecoder() {

            private final ErrorDecoder delegate = new Default();

            @Override
            public Exception decode(String methodKey, Response response) {
                log.error("methodKey={}, status={}", methodKey, response.status());

                try (final Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
                    String responseBody = IOUtils.toString(reader);

                    final ObjectNode node = new ObjectMapper().readValue(responseBody, ObjectNode.class);

                    String message = "FeignError=" + response.status();

                    if (node.has("message"))
                        message = node.get("message").textValue();

                    return new RuntimeException(message);

                } catch (Exception e) {
                    log.error("Error reading the response", e);
                }

                return delegate.decode(methodKey, response);
            }
        };
    }
}
