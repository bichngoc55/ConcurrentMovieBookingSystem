package com.example.ConcurrentMovieTicketBookingSystem.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private String ticketId;
    private Movie movie;
    private Seat seat;
    private String customerName;
    private String customerEmail;
    private LocalDateTime bookingTime;
    
    public Ticket(Movie movie, Seat seat, String customerName, String customerEmail) {
        this.ticketId = UUID.randomUUID().toString(); 
        this.movie = movie;
        this.seat = seat;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.bookingTime = LocalDateTime.now();
    }
    
    public String getTicketId() {
        return ticketId;
    }
    
    public Movie getMovie() {
        return movie;
    }
    
    public Seat getSeat() {
        return seat;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
}