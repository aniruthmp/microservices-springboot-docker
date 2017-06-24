package com.example.venue.db.repository;

import com.example.venue.db.domain.Venue;
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

    List<Venue> findByReservationId(Long reservationId);

    List<Venue> findByReservationIdAndIsActive(Long reservationId, boolean isActive);

    @Override
    Page<Venue> findAll(Pageable pageable);

}