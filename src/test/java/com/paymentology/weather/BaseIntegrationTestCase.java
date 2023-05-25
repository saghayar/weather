package com.paymentology.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentology.weather.domain.model.weather.WeatherResponseDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Files;
import java.nio.file.Paths;


@TestPropertySource(properties = {
        "spring.datasource.driver-class-name: org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:autotest;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.username: sa",

        "spring.jpa.hibernate.ddl-auto=none",

        "spring.liquibase.change-log=classpath:db/changelog-master.xml",
        "spring.liquibase.drop-first=true",
        "spring.liquibase.enabled=true",
        "service.open-meteo.url=http://localhost:30000",
        "service.ip-api.url=http://localhost:30000",
})
public class BaseIntegrationTestCase {

    public final static String ROOT_RESOURCES_DIR = "src/test/resources";

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    protected String readResourceFile(String path) {
        return Files.readString(Paths.get("src/test/resources/", path));
    }

    @SneakyThrows
    protected String getAsString(WeatherResponseDto message) {
        return objectMapper.writerWithDefaultPrettyPrinter()
                           .writeValueAsString(message);
    }

}
