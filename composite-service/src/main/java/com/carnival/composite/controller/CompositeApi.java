package com.carnival.composite.controller;

import com.carnival.composite.model.BookingDetail;
import com.carnival.composite.model.Reservation;
import com.carnival.composite.model.Result;
import com.carnival.composite.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * Created by a.c.parthasarathy
 */
@RestController
@RequestMapping("/composite")
public class CompositeApi {

    @Autowired
    private ServiceImpl serviceImpl;

    @GetMapping(value = "/names")
    public Resources<Object> names(@RequestHeader("Authorization") String token) {
        return this
                .serviceImpl
                .names(token);
    }

    @GetMapping(value = "/venues")
    public Result venues(@Param("page") int page, @Param("size") int size,
                         @RequestHeader("Authorization") String token) {
        return this
                .serviceImpl
                .venues(page, size, token);
    }

    @GetMapping(value = "/bookings")
    public BookingDetail bookings(@Param("id") long id,
                                  @RequestHeader("Authorization") String token) {
        return this
                .serviceImpl
                .bookings(id, token);
    }

    @PutMapping(value = "/booking/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> bookingUpdate(@Param("id") long id,
                                                @RequestBody Reservation reservation,
                                                @RequestHeader("Authorization") String token) {
        try {
            return this
                    .serviceImpl
                    .bookingUpdate(id, reservation, token);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
