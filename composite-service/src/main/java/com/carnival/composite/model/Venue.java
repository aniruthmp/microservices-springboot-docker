package com.carnival.composite.model;

import lombok.Data;

/**
 * Created by a.c.parthasarathy
 */
@Data
public class Venue {
    private String venueName;
    private boolean isAvailable;
    private long reservationId;
    private String bookingDate;
    private String personName;
}
