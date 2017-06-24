package com.example.composite.service;

import com.example.composite.api.ReservationReader;
import com.example.composite.api.VenueReader;
import com.example.composite.model.BookingDetail;
import com.example.composite.model.PublishModel;
import com.example.composite.model.Reservation;
import com.example.composite.model.Venue;
import com.example.composite.publish.KafkaProducer;
import com.example.composite.util.JsonUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Aniruth Parthasarathy
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
    @Cacheable(cacheNames = "bookings")
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
    public ResponseEntity<Void> bookingUpdate_(long id, Reservation reservation, String token) {
        log.info("Before Update.. For id = " + id + " ; " + reservation.toString());

        /**
         * Feign doesn't support PATCH. Hence, we can do only PUT
         */
        Reservation oldReservation = this
                .reservationReader
                .findById(id, token)
                .getContent();
        log.info("Got the reservation " + oldReservation.toString());
        if (Objects.nonNull(oldReservation)) {
            oldReservation.setFirstName(Objects.toString(reservation.getFirstName(),
                    oldReservation.getFirstName()));
            oldReservation.setLastName(Objects.toString(reservation.getLastName(),
                    oldReservation.getLastName()));
            Reservation updatedReservation = this
                    .reservationReader
                    .updateReservation(id, oldReservation, token)
                    .getContent();
            log.info("Updated reservation " + updatedReservation.toString());

            //We will update the reservationName in Venue service through Kafka Event
            PublishModel publishModel = new PublishModel();
            publishModel.setId(id);
            publishModel.setReservationName(reservation.getReservationName());
            kafkaProducer.sendMessage(TOPIC, JsonUtil.toJson(publishModel));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.info("Couldn't find any reservation for id: " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Void> bookingUpdate(long id, Reservation reservation, String token) {
        log.info("Before Update.. For id = " + id + " ; " + reservation.toString());

        try {
            log.info("Got the reservation " + reservation.toString());
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
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Void> bookingUpdateError(long id, Reservation reservation, String token) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
