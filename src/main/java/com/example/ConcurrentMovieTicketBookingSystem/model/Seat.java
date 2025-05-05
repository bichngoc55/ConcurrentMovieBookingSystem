package com.example.ConcurrentMovieTicketBookingSystem.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class Seat {
    private int seatNumber;
    private AtomicBoolean isAvailable;
    private String customerName; 
    private String customerEmail; 
    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.isAvailable = new AtomicBoolean(true); 
        this.customerName = null;
        this.customerEmail = null;
    }
    public int getSeatNumber() {
        return seatNumber;
    }
    public boolean isAvailable() {
        return isAvailable.get();
    }
    
      public boolean reserve(String customerName, String customerEmail) {
        if (isAvailable.compareAndSet(true, false)) {
            this.customerName = customerName;
            this.customerEmail = customerEmail;
            return true;
        }
        return false;
    }

    public boolean cancel() {
        if (isAvailable.compareAndSet(false, true)) {
            this.customerName = null;
            this.customerEmail = null;
            return true;
        }
        return false;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
