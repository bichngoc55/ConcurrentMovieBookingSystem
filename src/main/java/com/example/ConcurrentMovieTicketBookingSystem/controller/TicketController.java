package com.example.ConcurrentMovieTicketBookingSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ConcurrentMovieTicketBookingSystem.model.Movie;
import com.example.ConcurrentMovieTicketBookingSystem.model.Seat;
import com.example.ConcurrentMovieTicketBookingSystem.model.Ticket;
import com.example.ConcurrentMovieTicketBookingSystem.repository.SeatRepository;
import com.example.ConcurrentMovieTicketBookingSystem.service.TicketService;

@Controller
public class TicketController {
    private final SeatRepository seatRepo = new SeatRepository(20); // Tăng số lượng ghế lên 20
    private final TicketService ticketService = new TicketService();
    private final Movie movie = new Movie("Avengers: Endgame", "Action/Sci-Fi", 120000);

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("seats", seatRepo.getSeats());
        model.addAttribute("movie", movie);
        return "index";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAttribute("seats", seatRepo.getSeats());
        model.addAttribute("movie", movie);
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "admin";
    }

    @GetMapping("/booking-form")
    public String bookingForm(@RequestParam Integer seatNumber, Model model) {
        Seat seat = seatRepo.getSeat(seatNumber);
        
        if (seat == null || !seat.isAvailable()) {
            model.addAttribute("error", "Ghế số " + seatNumber + " không hợp lệ hoặc đã được đặt.");
            model.addAttribute("seats", seatRepo.getSeats());
            model.addAttribute("movie", movie);
            return "index";
        }
        
        model.addAttribute("seatNumber", seatNumber);
        model.addAttribute("movie", movie);
        return "booking-form";
    }

    @PostMapping("/reserve")
    public String reserveSeat(
            @RequestParam Integer seatNumber,
            @RequestParam String customerName,
            @RequestParam String customerEmail,
            Model model) {
        
        if (seatNumber == null) {
            model.addAttribute("error", "Số ghế không được để trống!");
            model.addAttribute("seats", seatRepo.getSeats());
            model.addAttribute("movie", movie);
            return "index";
        }
        
        if (customerName == null || customerName.trim().isEmpty()) {
            model.addAttribute("error", "Tên khách hàng không được để trống!");
            model.addAttribute("seatNumber", seatNumber);
            model.addAttribute("movie", movie);
            return "booking-form";
        }
        
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            model.addAttribute("error", "Email khách hàng không được để trống!");
            model.addAttribute("seatNumber", seatNumber);
            model.addAttribute("customerName", customerName);
            model.addAttribute("movie", movie);
            return "booking-form";
        }
        
        Seat seat = seatRepo.getSeat(seatNumber);
        boolean success = ticketService.reserveSeat(seat, customerName, customerEmail, movie);
        
        if (success) {
            Ticket ticket = ticketService.getTicket(seatNumber);
            model.addAttribute("message", "Đặt vé thành công cho ghế số " + seatNumber);
            model.addAttribute("ticket", ticket);
            return "booking-success";
        } else {
            model.addAttribute("error", "Ghế số " + seatNumber + " đã được đặt. Vui lòng chọn ghế khác.");
            model.addAttribute("seats", seatRepo.getSeats());
            model.addAttribute("movie", movie);
            return "index";
        }
    }
    
    @PostMapping("/cancel/{seatNumber}")
    public String cancelReservation(@PathVariable int seatNumber, Model model) {
        boolean success = ticketService.cancelReservation(seatNumber);
        
        if (success) {
            model.addAttribute("message", "Hủy đặt ghế số " + seatNumber + " thành công!");
        } else {
            model.addAttribute("error", "Không thể hủy đặt ghế số " + seatNumber + ".");
        }
        
        model.addAttribute("seats", seatRepo.getSeats());
        model.addAttribute("movie", movie);
        model.addAttribute("tickets", ticketService.getAllTickets());
        
        return "admin";
    }
    
    @GetMapping("/ticket/{seatNumber}")
    public String viewTicket(@PathVariable int seatNumber, Model model) {
        Ticket ticket = ticketService.getTicket(seatNumber);
        
        if (ticket != null) {
            model.addAttribute("ticket", ticket);
            model.addAttribute("movie", movie);
            return "ticket-details";
        } else {
            model.addAttribute("error", "Không tìm thấy thông tin vé cho ghế số " + seatNumber);
            model.addAttribute("seats", seatRepo.getSeats());
            model.addAttribute("movie", movie);
            return "index";
        }
    }
}