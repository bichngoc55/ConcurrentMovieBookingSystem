package com.example.ConcurrentMovieTicketBookingSystem;

import com.example.ConcurrentMovieTicketBookingSystem.model.Movie;
import com.example.ConcurrentMovieTicketBookingSystem.model.Seat;
import com.example.ConcurrentMovieTicketBookingSystem.repository.SeatRepository;
import com.example.ConcurrentMovieTicketBookingSystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CancelTicketTest {
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
    public void testCancelAndRebookSeat() {
        // Hủy vé xong → đặt lại ghế vừa huỷ được

        final int seatNumber = 5;
        Seat seat = seatRepo.getSeat(seatNumber);

        // Đầu tiên đặt vé
        boolean bookingResult = ticketService.reserveSeat(seat, "Nguyễn Văn A", "nguyenvana@email.com", movie);
        assertTrue(bookingResult, "Việc đặt vé ban đầu phải thành công");
        assertFalse(seat.isAvailable(), "Ghế phải được đánh dấu là đã đặt");

        // Hủy vé
        boolean cancelResult = ticketService.cancelReservation(seatNumber);
        assertTrue(cancelResult, "Việc hủy vé phải thành công");
        assertTrue(seat.isAvailable(), "Ghế phải được đánh dấu là khả dụng sau khi hủy vé");
        assertNull(ticketService.getTicket(seatNumber), "Thông tin vé phải bị xóa sau khi hủy");

        // Đặt lại ghế vừa hủy
        boolean rebookingResult = ticketService.reserveSeat(seat, "Trần Thị B", "tranthib@email.com", movie);
        assertTrue(rebookingResult, "Việc đặt lại vé sau khi hủy phải thành công");
        assertFalse(seat.isAvailable(), "Ghế phải được đánh dấu là đã đặt sau khi đặt lại");

        // Kiểm tra thông tin vé mới
        assertNotNull(ticketService.getTicket(seatNumber), "Vé mới phải được tạo");
        assertEquals("Trần Thị B", ticketService.getTicket(seatNumber).getCustomerName(), "Tên khách hàng mới phải khớp");
        assertEquals("tranthib@email.com", ticketService.getTicket(seatNumber).getCustomerEmail(), "Email khách hàng mới phải khớp");
    }

    @Test
    public void testCancelNonexistentTicket() {
        // Kiểm tra trường hợp hủy vé chưa đặt

        final int seatNumber = 10;

        // Kiểm tra ghế ban đầu còn trống
        assertTrue(seatRepo.getSeat(seatNumber).isAvailable(), "Ghế phải khả dụng ban đầu");

        // Thử hủy vé chưa đặt
        boolean cancelResult = ticketService.cancelReservation(seatNumber);
        assertFalse(cancelResult, "Không thể hủy vé chưa đặt");
    }
}
