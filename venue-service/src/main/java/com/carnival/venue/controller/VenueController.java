package com.carnival.venue.controller;

import com.carnival.venue.controller.service.VenueService;
import com.carnival.venue.db.domain.Venue;
import com.carnival.venue.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by a.c.parthasarathy on 10/18/16.
 */
@RestController
@Slf4j
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping(value = "/findAll")
    public Response findAll(@Param("page") int page,
                            @Param("size") int size) {
        return venueService.searchAll(page, size);
    }

    @GetMapping(value = "/findByReservationId")
    public List<Venue> findByReservationId(@Param("reservationId") long reservationId) {
        return venueService.searchByReservationId(reservationId);
    }

    @PutMapping(value = "/update/venue")
    public List<Venue> updateByReservationId(@Param("reservationId") long reservationId,
                                             @Param("personName") String personName) {
        return venueService.updateByReservationId(reservationId, personName);
    }
}
