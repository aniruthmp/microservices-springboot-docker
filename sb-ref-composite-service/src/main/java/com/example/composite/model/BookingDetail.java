package com.example.composite.model;

import lombok.Data;

import java.util.Collection;

/**
 * Created by a.c.parthasarathy
 */
@Data
public class BookingDetail {
    private Reservation reservation;
    private Collection<Venue> venues;
}