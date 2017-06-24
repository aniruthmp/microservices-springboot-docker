package com.example.composite.api;

import com.example.composite.model.Venue;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Aniruth Parthasarathy
 */
@FeignClient("sb-ref-venue-service")
public interface VenueReader {

    @RequestMapping(method = RequestMethod.GET, value = "/findByReservationId?reservationId={reservationId}")
    List<Venue> findByReservationId(@PathVariable("reservationId") long reservationId,
                                    @RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.PUT,
            value = "/update/venue?reservationId={reservationId}&personName={personName}")
    List<Venue> updateByReservationId(@PathVariable("reservationId") long reservationId,
                                      @PathVariable("personName") String personName,
                                      @RequestHeader("Authorization") String token);

}