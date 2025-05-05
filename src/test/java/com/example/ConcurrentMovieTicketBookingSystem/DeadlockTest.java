package com.example.ConcurrentMovieTicketBookingSystem;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.ConcurrentMovieTicketBookingSystem.model.Movie;
import com.example.ConcurrentMovieTicketBookingSystem.model.Seat;
import com.example.ConcurrentMovieTicketBookingSystem.repository.SeatRepository;
import com.example.ConcurrentMovieTicketBookingSystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class DeadlockTest {
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
    public void testInterleavedBookingCancellation() throws InterruptedException {
        // Tạo luồng đặt xen kẽ ghế → kiểm tra không bị kẹt luồng

        final int numThreads = 10;
        final int numOperations = 1000; // Số lượng lớn các hoạt động để tăng khả năng phát hiện deadlock
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch completionLatch = new CountDownLatch(numThreads);
        final AtomicBoolean deadlockDetected = new AtomicBoolean(false);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    startLatch.await(); // Đợi tất cả các luồng sẵn sàng

                    for (int j = 0; j < numOperations; j++) {
                        int seatNumber = (threadNum + j) % 20 + 1; // Đảm bảo các luồng thay đổi ghế và có khả năng xen kẽ
                        Seat seat = seatRepo.getSeat(seatNumber);

                        if (j % 2 == 0) {
                            // Thực hiện đặt ghế
                            ticketService.reserveSeat(seat, "Thread " + threadNum, "thread" + threadNum + "@example.com", movie);
                        } else {
                            // Thực hiện hủy ghế
                            ticketService.cancelReservation(seatNumber);
                        }

                        // Nhường CPU để tăng khả năng xen kẽ
                        Thread.yield();
                    }
                } catch (InterruptedException e) {
                    deadlockDetected.set(true);
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    deadlockDetected.set(true);
                    e.printStackTrace();
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        // Bắt đầu tất cả các luồng
        startLatch.countDown();

        // Đợi cho tất cả các luồng hoàn thành hoặc timeout
        boolean completed = completionLatch.await(30, TimeUnit.SECONDS);

        // Ngừng executor service
        executorService.shutdownNow();

        // Kiểm tra không có deadlock
        assertTrue(completed, "Tất cả các luồng phải hoàn thành mà không bị deadlock");
        assertFalse(deadlockDetected.get(), "Không nên phát hiện lỗi hoặc deadlock trong quá trình thực hiện");
    }

    @Test
    public void testComplexBookingPattern() throws InterruptedException {
        // Mô phỏng một mẫu đặt vé phức tạp với nhiều ghế

        final int numSeats = 20;
        final int numThreads = 5;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch completionLatch = new CountDownLatch(numThreads);

        // Tạo các luồng thực hiện các hoạt động đặt vé phức tạp
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    startLatch.await();

                    for (int j = 0; j < 50; j++) {
                        // Mỗi luồng chọn các ghế khác nhau dựa trên ID luồng
                        int seatToBook = (threadNum * 4 + j) % numSeats + 1;
                        int seatToCancel = (threadNum * 4 + j + 2) % numSeats + 1;

                        // Đặt một ghế
                        Seat bookSeat = seatRepo.getSeat(seatToBook);
                        ticketService.reserveSeat(bookSeat, "Thread " + threadNum, "thread" + threadNum + "@example.com", movie);

                        // Đợi một chút để tăng khả năng xen kẽ
                        Thread.sleep(1);

                        // Hủy một ghế khác
                        ticketService.cancelReservation(seatToCancel);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        // Bắt đầu tất cả các luồng
        startLatch.countDown();

        // Đợi cho tất cả các luồng hoàn thành hoặc timeout
        boolean completed = completionLatch.await(30, TimeUnit.SECONDS);

        // Ngừng executor service
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);

        // Kiểm tra không có deadlock
        assertTrue(completed, "Tất cả các luồng phải hoàn thành mà không bị deadlock");
        assertTrue(terminated, "ExecutorService phải kết thúc mà không bị kẹt");
    }
}

