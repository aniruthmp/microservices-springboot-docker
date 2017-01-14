package com.carnival.venue;

import com.carnival.jwt.CarnivalJWTConfig;
import com.carnival.log.LoggingAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created by a.c.parthasarathy
 */
@EnableEurekaClient
@EnableResourceServer
@EnableScheduling
@SpringBootApplication
@EnableCouchbaseRepositories(basePackages = "com.carnival.venue.db.repository")
@Import({CarnivalJWTConfig.class, LoggingAspect.class})
public class VenueApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenueApplication.class, args);
    }
}
