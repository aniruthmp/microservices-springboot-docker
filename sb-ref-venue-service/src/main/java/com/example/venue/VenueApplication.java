package com.example.venue;

import com.example.jwt.JWTConfig;
import com.example.log.LoggingAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Aniruth Parthasarathy
 */
@EnableSwagger2
@EnableDiscoveryClient
@EnableResourceServer
@EnableScheduling
@SpringBootApplication
@EnableCaching
@Import({JWTConfig.class, LoggingAspect.class, BeanValidatorPluginsConfiguration.class})
public class VenueApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenueApplication.class, args);
    }
}
