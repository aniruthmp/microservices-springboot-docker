package com.carnival.gateway.service;

import com.carnival.gateway.api.CompositeReader;
import com.carnival.gateway.model.Reservation;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by a.c.parthasarathy on 10/18/16.
 */
@Service
@Slf4j
public class GatewayService {


    @Autowired
    private CompositeReader compositeReader;

    public Collection<String> namesError(String token) {
        log.error("Came inside ReservationApiGateway::fallback");
        return Arrays.asList("A,B,C,D".split(","));
    }

    @HystrixCommand(fallbackMethod = "namesError")
    public Collection<String> basicNameInfo(String token) {
        log.info("Came inside basicNameInfo with token: {}", token);
        return this
                .compositeReader
                .names(token)
                .getContent()
                .stream()
                .map(Reservation::getReservationName)
                .collect(Collectors.toList());

    }

    public Object detailedNameInfo(String token) {
        log.info("Came inside detailedNameInfo with token: {}", token);
        return this
                .compositeReader
                .names(token)
                .getContent();

    }

    public Object venues(int page, int size, String token) {
        return this
                .compositeReader
                .getVenues(page, size, token);

    }

    public Object bookings(long id, String token) {
        return this
                .compositeReader
                .getBookingsByReservationId(id, token);
    }

    public ResponseEntity<String> bookingUpdate(long id, Reservation reservation, String token)
            throws ExecutionException, InterruptedException {
        return compositeReader.updateByReservationId(id, reservation, token);
    }

}
