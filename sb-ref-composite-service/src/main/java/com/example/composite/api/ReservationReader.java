package com.example.composite.api;

import com.example.composite.model.Reservation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Aniruth Parthasarathy
 */
@FeignClient("sb-ref-reservation-service")
public interface ReservationReader {

    @RequestMapping(method = RequestMethod.GET, value = "/reservations/{id}")
    Resource<Reservation> findById(@PathVariable("id") long id,
                                   @RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.PATCH, value = "/reservations/{id}")
    Resource<Reservation> updateReservation(@PathVariable("id") long id,
                                            @RequestBody Reservation reservation,
                                            @RequestHeader("Authorization") String token);

}