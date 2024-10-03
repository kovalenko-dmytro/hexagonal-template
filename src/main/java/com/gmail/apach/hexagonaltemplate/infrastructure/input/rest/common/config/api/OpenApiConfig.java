package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.config.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = OpenApi.SECURITY_SCHEME_NAME.getValue();
        return new OpenAPI()
            .components(
                new Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme(OpenApi.SECURITY_SCHEME.getValue())
                            .bearerFormat(OpenApi.BEARER_FORMAT.getValue())))
            .security(List.of(new SecurityRequirement().addList(securitySchemeName)))
            .info(info());
    }

    private Info info() {
        Info info = new Info();
        info.setTitle(OpenApi.OPEN_API_INFO_TITLE.getValue());
        return info;
    }
}
