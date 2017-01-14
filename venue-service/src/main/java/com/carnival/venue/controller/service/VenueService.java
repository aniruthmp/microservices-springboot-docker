package com.carnival.venue.controller.service;

import com.carnival.venue.db.domain.Venue;
import com.carnival.venue.db.repository.VenueRepository;
import com.carnival.venue.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by a.c.parthasarathy on 10/18/16.
 */
@Service
@Slf4j
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    public Response searchAll(int page, int size) {
        log.info("Came inside searchAll with page/size : " + page + "/" + size);
        Page<Venue> pVenues = venueRepository.findAll(new PageRequest(page, size));
        Response response = new Response();

        if (Objects.nonNull(pVenues)) {
            response.getResult().addAll(pVenues.getContent());
            response.setTotalPages(pVenues.getTotalPages());
            response.setTotalRecords(pVenues.getTotalElements());
            log.info("searchAll return count: " + response.getResult().size());
        }

        return response;
    }

    public List<Venue> searchByReservationId(long reservationId) {
        log.info("Came inside searchByReservationId with reservationId : " + reservationId);
        List<Venue> venues = venueRepository.findByReservationId(reservationId);

        if (!CollectionUtils.isEmpty(venues))
            log.info("searchByReservationId return count: " + venues.size());
        else
            log.info("searchByReservationId return count: 0");
        return venues;
    }

    public List<Venue> updateByReservationId(long reservationId, String personName) {
        log.info("Came inside updateByReservationId with reservationId : " + reservationId);
        List<Venue> venues = venueRepository.findByReservationId(reservationId);

        if (!CollectionUtils.isEmpty(venues)) {
            venues.parallelStream().forEach(venue -> {
                venue.setPersonName(personName);
                log.debug("Updated " + venue.toString());
                venueRepository.save(venue);
            });
            log.info("updateByReservationId return count: " + venues.size());
        }
        else
            log.info("updateByReservationId return count: 0");
        return venues;
    }

}
