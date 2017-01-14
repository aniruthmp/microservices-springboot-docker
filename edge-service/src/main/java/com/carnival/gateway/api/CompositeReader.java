package com.carnival.gateway.api;

import com.carnival.gateway.model.Reservation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by a.c.parthasarathy
 */
@FeignClient("composite-service")
public interface CompositeReader {

    @RequestMapping(method = RequestMethod.GET, value = "/composite/names")
    Resources<Reservation> names(@RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.GET, value = "/composite/venues?page={page}&size={size}")
    Object getVenues(@PathVariable("page") int page, @PathVariable("size") int size,
                     @RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.GET, value = "/composite/bookings?id={id}")
    Object getBookingsByReservationId(@PathVariable("id") long id,
                                      @RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.PUT,
            value = "/composite/booking/update?id={id}")
    ResponseEntity<String> updateByReservationId(@PathVariable("id") long id,
                                                 @RequestBody Reservation reservation,
                                                 @RequestHeader("Authorization") String token);

}