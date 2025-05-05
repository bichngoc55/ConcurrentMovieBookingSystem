package com.example.ConcurrentMovieTicketBookingSystem;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.ConcurrentMovieTicketBookingSystem.model.Movie;
import com.example.ConcurrentMovieTicketBookingSystem.model.Seat;
import com.example.ConcurrentMovieTicketBookingSystem.repository.SeatRepository;
import com.example.ConcurrentMovieTicketBookingSystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConcurrentBookingTest {
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
    public void testMultipleThreadsSameSeat() throws InterruptedException {
        // 10 thread cùng đặt cùng 1 ghế → chỉ 1 thread thành công

        final int numThreads = 10;
        final int seatNumber = 1;
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicInteger successCount = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // Tạo 10 luồng đặt cùng một ghế
        for (int i = 0; i < numThreads; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    latch.await(); // Đợi cho đến khi CountDownLatch = 0
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Seat seat = seatRepo.getSeat(seatNumber);
                String customerName = "Thread " + threadNum;
                String customerEmail = "thread" + threadNum + "@example.com";

                if (ticketService.reserveSeat(seat, customerName, customerEmail, movie)) {
                    successCount.incrementAndGet();
                }
            });
        }

        // Bắt đầu tất cả các luồng cùng lúc
        latch.countDown();

        // Đợi cho tất cả các luồng kết thúc
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        // Kiểm tra chỉ có 1 thread thành công
        assertEquals(1, successCount.get(), "Chỉ có một luồng nên đặt vé thành công");

        // Kiểm tra ghế đã được đánh dấu là đã đặt
        assertFalse(seatRepo.getSeat(seatNumber).isAvailable(), "Ghế phải được đánh dấu là đã đặt");
    }

    @Test
    public void testMultipleThreadsDifferentSeats() throws InterruptedException {
        // 50 thread đặt các ghế khác nhau → ghế không trùng nhau
        final int numThreads = 20; // Sử dụng 20 thread vì có 20 ghế
        final CountDownLatch latch = new CountDownLatch(1);
        final Set<Integer> bookedSeatNumbers = new HashSet<>();

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // Tạo các luồng đặt các ghế khác nhau
        for (int i = 0; i < numThreads; i++) {
            final int seatNumber = i + 1; // Ghế từ 1 đến 20
            executorService.submit(() -> {
                try {
                    latch.await(); // Đợi cho đến khi CountDownLatch = 0
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Seat seat = seatRepo.getSeat(seatNumber);
                String customerName = "Customer " + seatNumber;
                String customerEmail = "customer" + seatNumber + "@example.com";

                if (ticketService.reserveSeat(seat, customerName, customerEmail, movie)) {
                    synchronized (bookedSeatNumbers) {
                        bookedSeatNumbers.add(seatNumber);
                    }
                }
            });
        }

        // Bắt đầu tất cả các luồng cùng lúc
        latch.countDown();

        // Đợi cho tất cả các luồng kết thúc
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        // Kiểm tra tất cả các ghế đều được đặt
        assertEquals(numThreads, bookedSeatNumbers.size(), "Tất cả các ghế nên được đặt thành công");

        // Kiểm tra từng ghế đã đặt
        for (int i = 1; i <= numThreads; i++) {
            assertFalse(seatRepo.getSeat(i).isAvailable(), "Ghế " + i + " phải được đánh dấu là đã đặt");
            assertTrue(ticketService.isTicketBooked(i), "Vé cho ghế " + i + " phải được tạo");
        }
    }
}
