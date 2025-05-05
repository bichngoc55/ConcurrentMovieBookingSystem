package com.example.ConcurrentMovieTicketBookingSystem;

import com.example.ConcurrentMovieTicketBookingSystem.model.Movie;
import com.example.ConcurrentMovieTicketBookingSystem.model.Seat;
import com.example.ConcurrentMovieTicketBookingSystem.repository.SeatRepository;
import com.example.ConcurrentMovieTicketBookingSystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SingleThreadedBookingTest {
    private SeatRepository seatRepo;
    private TicketService ticketService;
    private Movie movie;

    @BeforeEach
    public void setup() {
        seatRepo = new SeatRepository(20);
        ticketService = new TicketService();
        movie = new Movie("Avengers: Endgame", "Action/Sci-Fi", 120000);
    }

    @Test
    public void testSingleSeatBooking() {
        // Đặt 1 vé → kiểm tra ghế đã được đánh dấu là đã đặt

        // Lấy ghế số 1
        Seat seat = seatRepo.getSeat(1);

        // Kiểm tra ghế ban đầu còn trống
        assertTrue(seat.isAvailable(), "Ghế phải khả dụng trước khi đặt");

        // Thực hiện đặt ghế
        boolean bookingResult = ticketService.reserveSeat(seat, "Nguyễn Văn A", "nguyenvana@email.com", movie);

        // Kiểm tra kết quả đặt vé thành công
        assertTrue(bookingResult, "Việc đặt vé phải thành công");

        // Kiểm tra ghế đã được đánh dấu là đã đặt
        assertFalse(seat.isAvailable(), "Ghế không còn khả dụng sau khi đặt");

        // Kiểm tra thông tin vé
        assertNotNull(ticketService.getTicket(1), "Vé phải được tạo và lưu trữ");
        assertEquals("Nguyễn Văn A", ticketService.getTicket(1).getCustomerName(), "Tên khách hàng phải khớp");
        assertEquals("nguyenvana@email.com", ticketService.getTicket(1).getCustomerEmail(), "Email khách hàng phải khớp");
    }
}
