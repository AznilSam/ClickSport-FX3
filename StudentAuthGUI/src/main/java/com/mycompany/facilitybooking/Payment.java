package com.mycompany.facilitybooking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Payment {
    private Facility facility;
    private LocalDate date;
    private LocalTime time;
    private int hoursBooked;
    private double totalPrice;

    public Payment(Facility facility, LocalDate date, LocalTime time, int hoursBooked) {
        this.facility = facility;
        this.date = date;
        this.time = time;
        this.hoursBooked = hoursBooked;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        this.totalPrice = facility.getPricePerHour() * hoursBooked;
    }

    public Facility getFacility() {
        return facility;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getHoursBooked() {
        return hoursBooked;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getBookingDateTime() {
        return LocalDateTime.of(date, time);
    }
}

