package com.example.venue.controller.service;

import com.example.venue.db.domain.Venue;
import com.example.venue.db.repository.VenueRepository;
import com.example.venue.job.ScheduledJob;
import com.example.venue.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by Aniruth Parthasarathy.
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

    @Cacheable(cacheNames = "venuesByReservationId")
    public List<Venue> searchByReservationId(long reservationId) {
        log.info("Came inside searchByReservationId with reservationId : " + reservationId);
        List<Venue> venues = venueRepository.findByReservationIdAndIsActive(reservationId, true);

        if (!CollectionUtils.isEmpty(venues))
            log.info("searchByReservationId return count: " + venues.size());
        else
            log.info("searchByReservationId return count: 0");
        return venues;
    }

    public ResponseEntity<Void> updateByReservationId(long reservationId, String personName) {
        log.info("Came inside updateByReservationId with reservationId : " + reservationId);
        List<Venue> venues = venueRepository.findByReservationId(reservationId);

        if (!CollectionUtils.isEmpty(venues)) {
            venues.parallelStream().forEach(venue -> {
                venue.setPersonName(personName);
                log.debug("Updated " + venue.toString());
                venueRepository.save(venue);
            });
            log.info("updateByReservationId updated count: " + venues.size());
            new ResponseEntity<Void>(HttpStatus.OK);
        }
        else
            log.info("updateByReservationId return count: 0");
        return new ResponseEntity<Void>(HttpStatus.NOT_MODIFIED);
    }

    public ResponseEntity<Void> deleteByReservationId(long reservationId) {
        log.info("Came inside deleteByReservationId with reservationId : " + reservationId);
        List<Venue> venues = venueRepository.findByReservationId(reservationId);

        if (!CollectionUtils.isEmpty(venues)) {
            venues.parallelStream().forEach(venue -> {
                venue.setActive(false);
                log.debug("Updated " + venue.toString());
                venueRepository.save(venue);
            });
            log.info("deleteByReservationId return count: " + venues.size());
            new ResponseEntity<Void>(HttpStatus.OK);
        }
        else
            log.info("deleteByReservationId return count: 0");
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    @Autowired
    ScheduledJob scheduledJob;

    public ResponseEntity<Void> populate() {
        log.info("Came inside populate ");
        try{
            scheduledJob.populateVenues();
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
