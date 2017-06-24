package com.example.reservation.config;


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
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.and(ant("/**"),
                        Predicates.not(ant("/error/**")),
                        Predicates.not(ant("/admin.*")),
                        Predicates.not(ant("/profile/**")),
                        Predicates.not(ant("/admin/**"))))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot Reference App - Reservation micro-service")
                .description("This micro-service talks to an Oracle database. Following concepts are demonstrated in this application\n" +
                        "1. Eureka based service registration\n" +
                        "2. Externalized configuration using Spring Cloud Config server\n" +
                        "3. Ability to change certain config values without the need to re-deploy the application\n" +
                        "4. Spring Data JPA based repository methods\n" +
                        "5. Spring Cache based on Caffeine\n" +
                        "6. Spring Security (JWT based authentication and authorization)\n" +
                        "7. Spring Actuator\n" +
                        "8. Aspect oriented logging for APIs\n")
                .termsOfServiceUrl("")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .contact(new Contact("Aniruth Parthasarathy", "", "A.C.Parthasarathy@accenture.com"))
                .build();
    }

}
