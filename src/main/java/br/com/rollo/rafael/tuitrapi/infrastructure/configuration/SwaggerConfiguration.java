package br.com.rollo.rafael.tuitrapi.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    Logger logger = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String SECURITY_SCHEME_KEY = "Authorization header";
    public static final String SCANNED_PACKAGE = "br.com.rollo.rafael.tuitrapi.application";

    @Bean
    public GroupedOpenApi publicAPI() {
        logger.info("Creating Swagger open API configuration.");

        return GroupedOpenApi.builder()
                .group("tuiter-api-public")
                .packagesToScan(SCANNED_PACKAGE)
                .build();
                // TO-DO ignore parameter types
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_KEY,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title("The Tuítr API")
                        .description("An API to serve clients inspired by the Twitter application.")
                        .version("v1.1.2")
                        .license(new License().name("MIT").url("https://www.mit.edu/~amini/LICENSE.md")))
                .externalDocs(new ExternalDocumentation()
                        .description("The Tuítr API repo")
                        .url("https://github.com/rafael-rollo/tuitr-api"));
    }

}
