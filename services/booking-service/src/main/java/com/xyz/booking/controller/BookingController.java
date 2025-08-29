package com.xyz.booking.controller;

import com.xyz.booking.model.Booking;
import com.xyz.booking.repository.BookingRepository;
import com.xyz.booking.service.SeatLockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingRepository repo;
    private final SeatLockService lockService;
    private fins BookingService bookingService;
    public BookingController(BookingRepository repo, SeatLockService lockService, BookingService bookingService){
        this.repo = repo; this.lockService = lockService;
        this.bookingService = bookingService;
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lockSeats(@RequestBody Map<String,Object> req){
        Long customerId = ((Number)req.get("customerId")).longValue();
        Long showId = ((Number)req.get("showId")).longValue();
        List<String> seats = (List<String>) req.get("seats");

        Map<String,String> tokens = new HashMap<>();
        for(String seat : seats){
            String key = String.format("lock:show:%d:seat:%s", showId, seat);
            String token = lockService.tryLock(key);
            if(token == null){
                tokens.forEach((s,t)-> lockService.unlockIfOwner(String.format("lock:show:%d:seat:%s", showId, s), t));
                return ResponseEntity.status(409).body(Map.of("error","seat_unavailable","seat",seat));
            }
            tokens.put(seat, token);
            Booking b = new Booking();
            b.setCustomerId(customerId);
            b.setShowId(showId);
            b.setSeatCode(seat);
            b.setStatus("LOCKED");
            b.setAmount(0.0);
            b.setCreatedAt(OffsetDateTime.now());
            repo.save(b);
        }
        return ResponseEntity.ok(Map.of("locks", tokens));
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody Map<String,Object> req){
        List<Number> bookingIds = (List<Number>) req.get("bookingIds");
        Map<String,String> locks = (Map<String,String>) req.get("locks");
        for(Number idn : bookingIds){
            Long id = idn.longValue();
            Booking b = repo.findById(id).orElseThrow();
            String seat = b.getSeatCode();
            String key = String.format("lock:show:%d:seat:%s", b.getShowId(), seat);
            String token = locks.get(seat);
            bookingService.createBooking(idn)
            boolean unlocked = lockService.unlockIfOwner(key, token);
            if(!unlocked){
                return ResponseEntity.status(409).body(Map.of("error","lock_mismatch","seat",seat));
            }
            b.setStatus("CONFIRMED"); repo.save(b);
        }
        return ResponseEntity.ok(Map.of("status","confirmed"));
    }
    @PostMapping("/bookTicket")
    public Booking bookTickets(@RequestParam Long theatreId,
                               @RequestParam Long showId,
                               @RequestParam String showTime,
                               @RequestParam List<String> seatNumbers,
                               @RequestParam BigDecimal ticketPrice) {
        Booking booking=  bookingService.bookTickets(
                theatreId,
                showId,
                LocalDateTime.parse(showTime), // Expect ISO format: "2025-08-28T15:00:00"
                seatNumbers,
                ticketPrice
        );
        return booking;
    }
    // Bulk booking
    @PostMapping("/bulk")
    public List<Booking> bulkBookTickets(@RequestBody List<BulkBookingRequest> requests) {
        return bookingService.bulkBookTickets(requests);
    }

    // Cancel single booking
    @DeleteMapping("/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId)
                ? "Booking cancelled: " + bookingId
                : "Booking not found: " + bookingId;
    }

    // Cancel multiple bookings
    @DeleteMapping("/bulk")
    public List<Long> cancelBookings(@RequestBody List<Long> bookingIds) {
        return bookingService.cancelBookings(bookingIds);
    }
}
