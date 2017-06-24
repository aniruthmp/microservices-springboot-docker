package com.example.composite.controller;

import com.example.composite.model.BookingDetail;
import com.example.composite.model.Reservation;
import com.example.composite.service.ServiceImpl;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Aniruth Parthasarathy
 */
@RestController
@RequestMapping("/")
@Slf4j
@Api(value = "/", tags = "Bookings", description = "Composite Service")
public class CompositeApi {

    @Autowired
    private ServiceImpl serviceImpl;

    @GetMapping(value = "/bookings")
    @ApiOperation(
            value = "Find all booking records for a given Reservation Id",
            notes = "Find all booking records for a given Reservation Id",
            response = BookingDetail.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Bookings Not Found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    public BookingDetail bookings(@RequestParam("id") long id,
                                  @RequestHeader("Authorization") String token) {
        return this
                .serviceImpl
                .bookings(id, token);
    }

    @PutMapping(value = "/booking/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "Update booking records by Reservation Id",
            notes = "Update the full name for venue records by Reservation Id",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Venues Not Found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Void> bookingUpdate(@RequestParam("id") long id,
                                              @RequestBody Reservation reservation,
                                              @RequestHeader("Authorization") String token) {
        return this
                .serviceImpl
                .bookingUpdate(id, reservation, token);
    }
}