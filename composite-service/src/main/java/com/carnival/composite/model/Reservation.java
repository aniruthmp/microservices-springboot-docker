package com.carnival.composite.model;

/**
 * Created by a.c.parthasarathy
 */
public class Reservation {
    private String firstName;
    private String lastName;

    public String getReservationName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", reservationName='" + getReservationName() + '\'' +
                '}';
    }
}
