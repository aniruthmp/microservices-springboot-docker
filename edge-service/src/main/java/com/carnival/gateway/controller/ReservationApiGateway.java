package com.carnival.gateway.controller;

import com.carnival.gateway.model.Reservation;
import com.carnival.gateway.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * Created by a.c.parthasarathy
 */
@RestController
@RequestMapping("/edge")
@Slf4j
public class ReservationApiGateway {

    @Autowired
    private GatewayService gatewayService;

    @GetMapping(value = "/mobile/names")
    public Collection<String> basicNameInfo(@RequestHeader("Authorization") String token) {
        return this
                .gatewayService
                .basicNameInfo(token);
    }

    @GetMapping(value = "/desktop/names")
    public Object detailedNameInfo(@RequestHeader("Authorization") String token) {
        return this
                .gatewayService
                .detailedNameInfo(token);
    }

    @GetMapping(value = "/venues")
    public Object venues(@Param("page") int page, @Param("size") int size,
                         @RequestHeader("Authorization") String token) {
        return this
                .gatewayService
                .venues(page, size, token);
    }

    @GetMapping(value = "/bookings")
    public Object bookings(@Param("id") long id, @RequestHeader("Authorization") String token) {
        return this
                .gatewayService
                .bookings(id, token);
    }

    @PutMapping(value = "/booking/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> bookingUpdate(@Param("id") long id, @RequestBody Reservation reservation,
                                                @RequestHeader("Authorization") String token) {
        try {
            return this
                    .gatewayService
                    .bookingUpdate(id, reservation, token);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
