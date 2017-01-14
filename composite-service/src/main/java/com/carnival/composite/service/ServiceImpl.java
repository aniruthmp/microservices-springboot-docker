package com.carnival.composite.service;

import com.carnival.composite.api.ReservationReader;
import com.carnival.composite.api.VenueReader;
import com.carnival.composite.model.*;
import com.carnival.composite.publish.KafkaProducer;
import com.carnival.composite.util.JsonUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by a.c.parthasarathy on 10/18/16.
 */
@Service
@Slf4j
public class ServiceImpl {


    @Value("${spring.cloud.stream.bindings.output.destination}")
    private String TOPIC;

    @Autowired
    private ReservationReader reservationReader;

    @Autowired
    private VenueReader venueReader;

    @Autowired
    private KafkaProducer kafkaProducer;

    public Resources<Object> names(String token) {
        return this
                .reservationReader
                .read(token);

    }

    private Result venuesError(int page, int size, String token) {
        Venue v = new Venue();
        v.setVenueName("Dummy Venue");
        v.setReservationId(13L);
        v.setBookingDate("2007-09-01 10:14:33");
        List<Venue> venues = new ArrayList<>();
        venues.add(v);

        Result result = new Result();
        result.getResult().addAll(venues);
        result.setTotalPages(1);
        result.setTotalRecords(venues.size());
        return result;
    }

    @HystrixCommand(fallbackMethod = "venuesError")
    public Result venues(int page, int size, String token) {
        return this
                .venueReader
                .findAll(page, size, token);

    }

    private BookingDetail bookingsError(long id, String token) {
        Venue v = new Venue();
        v.setVenueName("Dummy Venue");
        v.setReservationId(id);
        v.setBookingDate("2007-09-01 10:14:33");
        List<Venue> venues = new ArrayList<>();
        venues.add(v);
        Reservation reservation = new Reservation();
        reservation.setFirstName("Dummy");
        reservation.setLastName("Name");

        BookingDetail bd = new BookingDetail();
        bd.setReservation(reservation);
        bd.setVenues(venues);
        return bd;
    }

    @HystrixCommand(fallbackMethod = "bookingsError")
    public BookingDetail bookings(long id, String token) {

        Reservation reservation = this
                .reservationReader
                .findById(id, token)
                .getContent();
        log.info("Got the reservation " + reservation.toString());
        List<Venue> venues = this
                .venueReader
                .findByReservationId(id, token);
        log.info("Got the venues " + venues.toString());

        BookingDetail bd = new BookingDetail();
        bd.setReservation(reservation);
        bd.setVenues(venues);
        return bd;
    }

    @HystrixCommand(fallbackMethod = "bookingUpdateError")
    public ResponseEntity<String> bookingUpdate(long id, Reservation reservation, String token)
            throws ExecutionException, InterruptedException {
        log.info("Before Update.. For id = " + id + " ; " + reservation.toString());
        Reservation updatedReservation = this
                .reservationReader
                .updateReservation(id, reservation, token)
                .getContent();
        log.info("Updated reservation " + updatedReservation.toString());

        //We will update the reservationName in Venue service through Kafka Event
        PublishModel publishModel = new PublishModel();
        publishModel.setId(id);
        publishModel.setReservationName(reservation.getReservationName());
        kafkaProducer.sendMessage(TOPIC, JsonUtil.toJson(publishModel));

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        return responseEntity;
    }

    private ResponseEntity<String> bookingUpdateError(long id, Reservation reservation, String token) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
