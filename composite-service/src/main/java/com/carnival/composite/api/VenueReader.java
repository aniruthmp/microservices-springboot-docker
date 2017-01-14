package com.carnival.composite.api;

import com.carnival.composite.model.Result;
import com.carnival.composite.model.Venue;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by a.c.parthasarathy
 */
@FeignClient("venue-service")
public interface VenueReader {

    @RequestMapping(method = RequestMethod.GET, value = "/findAll?page={page}&size={size}")
    Result findAll(@PathVariable("page") int page, @PathVariable("size") int size,
                   @RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.GET, value = "/findByReservationId?reservationId={reservationId}")
    List<Venue> findByReservationId(@PathVariable("reservationId") long reservationId,
                                    @RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.PUT,
            value = "/update/venue?reservationId={reservationId}&personName={personName}")
    List<Venue> updateByReservationId(@PathVariable("reservationId") long reservationId,
                                      @PathVariable("personName") String personName,
                                      @RequestHeader("Authorization") String token);

}