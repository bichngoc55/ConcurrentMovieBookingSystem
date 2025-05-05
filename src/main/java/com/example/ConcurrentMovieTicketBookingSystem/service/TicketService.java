package com.example.ConcurrentMovieTicketBookingSystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.ConcurrentMovieTicketBookingSystem.model.Movie;
import com.example.ConcurrentMovieTicketBookingSystem.model.Seat;
import com.example.ConcurrentMovieTicketBookingSystem.model.Ticket;

@Service
public class TicketService {
    // Sử dụng ConcurrentHashMap để lưu trữ thông tin vé đã đặt
    private ConcurrentHashMap<Integer, Ticket> bookedTickets = new ConcurrentHashMap<>();
    
    // Đặt vé với thông tin khách hàng
    public boolean reserveSeat(Seat seat, String customerName, String customerEmail, Movie movie) {
        if (seat != null && seat.isAvailable()) {
            if (seat.reserve(customerName, customerEmail)) {
                Ticket ticket = new Ticket(movie, seat, customerName, customerEmail);
                bookedTickets.put(seat.getSeatNumber(), ticket);
                return true;
            }
        }
        return false;
    }
    
    // Hủy đặt vé
    public boolean cancelReservation(int seatNumber) {
        Ticket ticket = bookedTickets.get(seatNumber);
        if (ticket != null) {
            Seat seat = ticket.getSeat();
            if (seat.cancel()) {
                bookedTickets.remove(seatNumber);
                return true;
            }
        }
        return false;
    }
    
    // Lấy thông tin vé theo số ghế
    public Ticket getTicket(int seatNumber) {
        return bookedTickets.get(seatNumber);
    }
    
    // Lấy danh sách tất cả các vé đã đặt
    public List<Ticket> getAllTickets() {
        return new ArrayList<>(bookedTickets.values());
    }
    
    // Kiểm tra vé đã đặt theo số ghế
    public boolean isTicketBooked(int seatNumber) {
        return bookedTickets.containsKey(seatNumber);
    }
}