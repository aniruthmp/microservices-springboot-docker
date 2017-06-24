package com.example.actuator.health;


import java.util.Collection;

import com.example.venue.db.domain.Venue;
import com.example.venue.db.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * The VenueHealthIndicator is a custom Spring Boot Actuator HealthIndicator
 * implementation. HealthIndicator classes are invoked when the Actuator
 * 'health' endpoint is invoked. Each HealthIndicator class assesses some
 * portion of the application's health, returing a Health object which indicates
 * that status and, optionally, additional health attributes.
 * <p>
 * created by Aniruth Parthasarathy
 */
@Component
public class VenueHealthIndicator implements HealthIndicator {

    /**
     * The VenueRepository business service.
     */
    @Autowired
    private VenueRepository venueRepository;

    @Override
    public Health health() {

        // Assess the application's Reservation health. If the application's
        // Venue components have data to service user requests, the Venue
        // component is considered 'healthy', otherwise it is not.

        Collection<Venue> venues = (Collection<Venue>) venueRepository.findAll();

        if (CollectionUtils.isEmpty(venues)) {
            return Health.down().withDetail("count", 0).build();
        } else {
            return Health.up().withDetail("count", venues.size()).build();
        }
    }
}
