package com.example.venue.controller;

import com.example.venue.controller.service.VenueService;
import com.example.venue.db.domain.Venue;
import com.example.venue.model.Response;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Aniruth Parthasarathy
 */
@RestController
@RequestMapping("/")
@Api(value = "/", tags = "Venues", description = "Operations about venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping(value = "/findAll")
    @ApiOperation(
            value = "Find all venue records for a given page and size",
            notes = "Expensive operation as it retrieves all the records",
            response = Response.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Venues Not Found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    public Response findAll(@ApiParam(value = "page no", required = true) @RequestParam("page") int page,
                            @ApiParam(value = "page size", required = true) @RequestParam("size") int size) {
        return venueService.searchAll(page, size);
    }

    @GetMapping(value = "/findByReservationId")
    @ApiOperation(
            value = "Find all venue records for a given Reservation Id",
            notes = "Find all venue records for a given Reservation Id",
            response = Venue.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Venues Not Found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    public List<Venue> findByReservationId(@ApiParam(value = "Reservation Id", required = true)
                                           @RequestParam("reservationId") long reservationId) {
        return venueService.searchByReservationId(reservationId);
    }

    @PutMapping(value = "/update/")
    @ApiOperation(
            value = "Update venue records by Reservation Id",
            notes = "Update the full name for venue records by Reservation Id",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Venues Not Found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Void> updateByReservationId(
            @ApiParam(value = "Reservation Id", required = true) @RequestParam("reservationId") long reservationId,
            @ApiParam(value = "Full Name", required = true) @RequestParam("personName") String personName) {
        return venueService.updateByReservationId(reservationId, personName);
    }

    @DeleteMapping(value = "/deleteByReservationId")
    @ApiOperation(
            value = "Delete all venue records by Reservation Id",
            notes = "Internally the required records are made Inactive instead of hard delete",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid Reservation Id")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Void> deleteByReservationId(@ApiParam(value = "Reservation Id", required = true)
                                                      @RequestParam("reservationId") long reservationId) {
        return venueService.deleteByReservationId(reservationId);
    }

    @PutMapping(value = "/populate")
    @ApiOperation(
            value = "Populate venue records",
            notes = "Populate from json file",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Venues Not Found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Void> populate() {
        return venueService.populate();
    }
}

