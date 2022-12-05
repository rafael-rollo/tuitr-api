package br.com.rollo.rafael.tuitrapi.configuration;

import br.com.rollo.rafael.tuitrapi.domain.posts.Post;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static String SCANNED_PACKAGE = "br.com.rollo.rafael.tuitrapi.application";

    Logger logger = LoggerFactory.getLogger(SwaggerConfiguration.class);

    @Bean
    public Docket builder() {	
        logger.info("Creating Swagger docket configuration.");
        
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(SwaggerConfiguration.SCANNED_PACKAGE))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .ignoredParameterTypes(User.class)
                .ignoredParameterTypes(Post.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "The Tuítr API",
                "An API to serve clients inspired by the Twitter application.",
                "1.0",
                "urn:tos",
                new Contact("Rafael Rollo", "", "rafaelrollo92@gmail.com"),
                "MIT License",
                "https://www.mit.edu/~amini/LICENSE.md",
                Collections.emptyList());
    }

    private SecurityContext securityContext() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");

        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        List<SecurityReference> references = Arrays.asList(new SecurityReference("JWT", authorizationScopes));

        return SecurityContext.builder().securityReferences(references).build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

}
