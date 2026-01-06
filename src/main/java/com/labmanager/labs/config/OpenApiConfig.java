package com.labmanager.labs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI labsOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("LabManager - Labs Service API")
                .version("1.0")
                .description("API za upravljanje laboratorijev, opreme in rezervacij (minimalna OpenAPI dokumentacija)."));
    }

}
