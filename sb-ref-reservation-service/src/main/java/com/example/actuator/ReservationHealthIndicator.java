package com.example.actuator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.example.reservation.db.domain.Reservation;
import com.example.reservation.db.repository.ReservationRepository;

/**
 * The ReservationHealthIndicator is a custom Spring Boot Actuator HealthIndicator
 * implementation. HealthIndicator classes are invoked when the Actuator
 * 'health' endpoint is invoked. Each HealthIndicator class assesses some
 * portion of the application's health, returing a Health object which indicates
 * that status and, optionally, additional health attributes.
 *
 * created by Paul Milazzo
 */
@Component
public class ReservationHealthIndicator implements HealthIndicator {

    /**
     * The ReservationRepository business service.
     */
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Health health() {

        // Assess the application's Reservation health. If the application's
        // Greeting components have data to service user requests, the Greeting
        // component is considered 'healthy', otherwise it is not.

        Collection<Reservation> reservations = (Collection<Reservation>) reservationRepository.findAll();

        if (reservations == null || reservations.size() == 0) {
            return Health.down().withDetail("count", 0).build();
        }

        return Health.up().withDetail("count", reservations.size()).build();
    }

}
