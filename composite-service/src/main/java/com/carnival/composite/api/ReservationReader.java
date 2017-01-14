package com.carnival.composite.api;

import com.carnival.composite.model.Reservation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

/**
 * Created by a.c.parthasarathy
 */
@FeignClient("reservation-service")
public interface ReservationReader {

    @RequestMapping(method = RequestMethod.GET, value = "/reservations")
    Resources<Object> read(@RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.GET, value = "/reservations/{id}")
    Resource<Reservation> findById(@PathVariable("id") long id,
                                   @RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.PUT, value = "/reservations/{id}")
    Resource<Reservation> updateReservation(@PathVariable("id") long id,
                                            @RequestBody Reservation reservation,
                                            @RequestHeader("Authorization") String token);

}