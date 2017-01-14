package com.carnival.venue.db.repository;

import com.carnival.venue.db.domain.Venue;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by a.c.parthasarathy
 */
@Repository
public interface VenueRepository extends CouchbasePagingAndSortingRepository<Venue, Long> {

    List<Venue> findByReservationId(long reservationId);

    Page<Venue> findAll(Pageable pageable);
}