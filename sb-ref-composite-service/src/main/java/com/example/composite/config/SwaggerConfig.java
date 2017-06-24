package com.example.composite.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.ant;

/**
 * Created by Aniruth Parthasarathy
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.composite.controller"))
                .paths(Predicates.and(ant("/**"),
                        Predicates.not(ant("/error/**")),
                        Predicates.not(ant("/admin/**"))))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot Reference App - Venue micro-service")
                .description("This basically forms the service gateways. Following concepts are demonstrated in this application\n" +
                        "1. Eureka based service registration\n" +
                        "2. Externalized configuration using Spring Cloud Config server\n" +
                        "3. Feign Client - for load balancing\n" +
                        "4. Spring Security (JWT based authentication and authorization)\n" +
                        "5. Spring Actuator\n" +
                        "6. Aspect oriented logging for APIs\n")
                .termsOfServiceUrl("")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .contact(new Contact("Aniruth Parthasarathy", "", "A.C.Parthasarathy@accenture.com"))
                .build();
    }

}