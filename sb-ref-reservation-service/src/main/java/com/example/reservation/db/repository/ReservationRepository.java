package com.example.reservation.db.repository;

import com.example.reservation.db.domain.Reservation;
import io.swagger.annotations.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by Aniruth Parthasarathy
 */
@RepositoryRestResource
public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long> {

    @ApiOperation(
            value = "Get all Reservations",
            notes = "Returns all Reservation records",
            response = Reservation.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No records found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    List<Reservation> findAll();

    @ApiOperation(
            value = "Find Reservation by LastName",
            notes = "Returns a record of Reservation",
            response = Reservation.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Invalid LastName")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    List<Reservation> findByLastNameIgnoreCase(
            @ApiParam(name = "ln", type = "query", value = "Last Name", required = true) @Param("ln") String name);

    @ApiOperation(
            value = "Find Reservation by FirstName",
            notes = "Returns a record of Reservation",
            response = Reservation.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Invalid FirstName")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    List<Reservation> findByFirstNameIgnoreCase(
            @ApiParam(name = "fn", type = "query", value = "First Name", required = true) @Param("fn") String name);

    @ApiOperation(
            value = "Find Reservation by partial FirstName",
            notes = "Returns a record of Reservation",
            response = Reservation.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No records found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    List<Reservation> findByFirstNameContainingIgnoreCase(
            @ApiParam(name = "fn", type = "query", value = "Partial First Name", required = true) @Param("fn") String name);

    @ApiOperation(
            value = "Find Reservation by Last and First Name",
            notes = "Returns a record of Reservation",
            response = Reservation.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No records found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    List<Reservation> findByLastNameAndFirstNameIgnoreCase(
            @ApiParam(name = "ln", type = "query", value = "Last Name", required = true) @Param("ln") String lastname,
            @ApiParam(name = "fn", type = "query", value = "First Name", required = true) @Param("fn") String firstname);

    @ApiOperation(
            value = "Find count of Reservation by Last",
            notes = "Returns a record of Reservation",
            response = Reservation.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No records found")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer eyJhbGciOiJSUzI1NiI...",
                    required = true, dataType = "string", paramType = "header")})
    int countByLastNameIgnoreCase(
            @ApiParam(name = "ln", type = "query", value = "Last Name", required = true)
            @Param("ln") String name);
}

